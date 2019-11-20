package com.mrcd.xrouter.compiler.core;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.annotation.XPath;
import com.mrcd.xrouter.compiler.bean.ClassPath;
import com.mrcd.xrouter.compiler.bean.RouterParam;
import com.mrcd.xrouter.compiler.core.databinder.DataBindCodeFactory;
import com.mrcd.xrouter.compiler.exception.XRouterException;
import com.mrcd.xrouter.compiler.utils.Constant;
import com.mrcd.xrouter.compiler.utils.FileUtils;
import com.mrcd.xrouter.compiler.utils.ProcessUtils;
import com.mrcd.xrouter.compiler.utils.TextUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 自定义的编译器处理器
 */
@AutoService(Processor.class)
@SupportedOptions(Constant.KEY_MODULE_NAME)
@SupportedAnnotationTypes({RouterProcess.PATH_ANNOTATION, RouterProcess.PARAM_ANNOTATION})
public class RouterProcess extends AbstractProcessor {

    public static final String PATH_ANNOTATION = "com.mrcd.xrouter.annotation.XPath";

    public static final String PARAM_ANNOTATION = "com.mrcd.xrouter.annotation.XParam";

    /**
     * 同一个library下路由名重复
     */
    private static final String REPEATED_ROUTER_MSG = "%s are already defined repeatedly >>> %s  %s";

    /**
     * 不合法的路由名，路由名字只能是大小写英文字母，并且在生成代码的过程中会主动将第一个字母小写
     */
    private static final String ILLEGAL_ROUTER_NAME = "The route name can only be english letters >>>>>> %s";

    private static final String BUILD_GRADLE_CONFIG = "javaCompileOptions { annotationProcessorOptions { arguments = [MODULE_NAME: project.getName()] } }";

    private static final String ILLEGAL_MODULE_NAME = "The module name is empty,please config build.gradle like this  >>>>>> " + BUILD_GRADLE_CONFIG;

    /**
     * 存放缓存JSon数据的目录
     */
    private static final String CLASS_CACHE_DIR = "/ProcessCache/";

    /**
     * 缓存json文件名
     */
    private static final String CLASS_CACHE_FILE = "ClassRouter.json";

    private Filer mFiler;
    private Elements mElementUtils;

    /**
     * intent的类声明
     */
    private ClassName mIntentName;
    /**
     * intentWrap的类声明
     */
    private ClassName mIntentWrap;

    /**
     * 库的名字，注意，此处的数据依赖一库中的compileOption配置
     */
    private String mModuleName;

    /**
     * JSon缓存文件对象
     */
    private File mCacheFile;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
        initClassCacheFile();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> types = new LinkedHashSet<>();
        types.add(XPath.class.getName());
        types.add(XParam.class.getName());
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return processXPath(set, roundEnvironment);
    }

    /**
     * 对XPath注解进行处理
     *
     * @param set              扫描到的类或者接口集合
     * @param roundEnvironment 编译环境
     * @return 是否处理过了注解
     */
    private boolean processXPath(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(XPath.class);
        if (null != elements && elements.size() > 0) {
            System.err.println("------>>start compile");
            initClassDeclaration();
            writeCacheFile(elements);
            return true;
        }
        return false;
    }

    /**
     * 初始化缓存文件
     */
    private void initClassCacheFile() {
        Map<String, String> options = processingEnv.getOptions();
        if (null != options && options.size() > 0) {
            mModuleName = options.get(Constant.KEY_MODULE_NAME);
        }
        System.err.println("ModuleName: " + mModuleName);
        if (TextUtils.isEmpty(mModuleName)) {
            throw new XRouterException(ILLEGAL_MODULE_NAME);
        }
        File dir = new File(mModuleName, CLASS_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mCacheFile = new File(dir, CLASS_CACHE_FILE);
        System.err.println("CacheRouterFile: " + mCacheFile);
        try {
            if (mCacheFile.exists()) {
                mCacheFile.delete();
            }
            //创建新文件
            mCacheFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化类声明，在生成代码时需要使用
     */
    private void initClassDeclaration() {
        mIntentName = ClassName.get(Constant.PKG_ANDROID_CONTENT, Constant.INTENT);
        mIntentWrap = ClassName.get(Constant.CORE_PKG_NAME, Constant.INTENT_WRAPPER);
    }

    /**
     * 扫描信息，序列化
     *
     * @param elements 类或者接口集合
     */
    private void writeCacheFile(Set<? extends Element> elements) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(mCacheFile));
            List<ClassPath> paths = new ArrayList<>();
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS) {
                    List<RouterParam> params = generateDataBinder(element);
                    ClassPath path = generatePathBean(element);
                    if (null != path) {
                        path.setParams(params);
                        ClassPath existPath = checkUniqueness(paths, path);
                        if (null != existPath) {
                            String existName = existPath.getClassName();
                            String currentName = path.getClassName();
                            String pathName = existPath.getPath();
                            String msg = String.format(REPEATED_ROUTER_MSG, pathName, existName, currentName);
                            throw new XRouterException(msg);
                        }
                        paths.add(path);
                    }
                }
            }
            writer.write(new Gson().toJson(paths));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.close(writer);
        }
    }

    private ClassPath generatePathBean(Element element) {
        XPath xPath = element.getAnnotation(XPath.class);
        if (xPath != null) {
            String path = xPath.path();
            String simpleName = ProcessUtils.getSimpleClassName(element);
            String className = ProcessUtils.getClassName(processingEnv, element);
            path = TextUtils.toUpperCaseFirstOne(TextUtils.isEmpty(path) ? simpleName : path);
            checkPathName(path);
            return new ClassPath(path, simpleName, className);
        }
        return null;
    }

    /**
     * 生成DataBinder类，并且更加扫描到的信息，返回类中的参数注解信息
     *
     * @param element 类对象
     * @return 参数集
     */
    private List<RouterParam> generateDataBinder(Element element) {
        List<RouterParam> params = new ArrayList<>();
        TypeElement typeElement = (TypeElement) element;
        List<? extends Element> members = mElementUtils.getAllMembers(typeElement);
        if (null != members && members.size() > 0) {
            MethodSpec.Builder bindDataMethodBuilder = generateBindDataMethod(element);
            bindDataMethodBuilder.beginControlFlow("if (null != target && null != target.getIntent())")
                                 .addStatement("$T intent = target.getIntent()", mIntentName);

            MethodSpec.Builder releaseMethodBuilder = generateReleaseMethod(element);
            releaseMethodBuilder.beginControlFlow("if (null != target)");

            for (Element item : members) {
                DataBindCodeFactory codeFactory = DataBindCodeFactory.getInstance();
                RouterParam param = codeFactory.generate(bindDataMethodBuilder, releaseMethodBuilder, item);
                if (param != null) {
                    params.add(param);
                }
            }
            bindDataMethodBuilder.endControlFlow();
            releaseMethodBuilder.endControlFlow();
            String simpleClassName = ProcessUtils.getSimpleClassName(element);
            TypeSpec.Builder classBuilder = generateBinderClass(simpleClassName, bindDataMethodBuilder, releaseMethodBuilder);
            String pkg = ProcessUtils.getPackage(processingEnv, element);
            JavaFile javaFile = JavaFile.builder(pkg, classBuilder.build()).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    public TypeSpec.Builder generateBinderClass(String className, MethodSpec.Builder binderMethod, MethodSpec.Builder releaseMethod) {
        return TypeSpec.classBuilder(className + Constant.DATA_BINDER_SUFFIX)
                       .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                       .addMethod(binderMethod.build())
                       .addMethod(releaseMethod.build());
    }

    private Builder generateReleaseMethod(Element element) {
        return MethodSpec.methodBuilder("releaseData")
                         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                         .addParameter(ClassName.get(element.asType()), "target")
                         .addParameter(mIntentWrap, "wrapper")
                         .returns(void.class);
    }

    private Builder generateBindDataMethod(Element element) {
        return MethodSpec.methodBuilder("bindData")
                         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                         .addParameter(ClassName.get(element.asType()), "target")
                         .addParameter(mIntentWrap, "wrapper")
                         .returns(void.class);
    }


    public static void checkPathName(String path) {
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher m = pattern.matcher(path);
        if (!m.matches()) { //匹配不到,说明路由名称不匹配
            throw new XRouterException(String.format(ILLEGAL_ROUTER_NAME, path));
        }
    }

    public static ClassPath checkUniqueness(List<ClassPath> paths, ClassPath path) {
        for (ClassPath itemPath : paths) {
            if (itemPath.equals(path)) {
                return itemPath;
            }
        }
        return null;
    }
}
