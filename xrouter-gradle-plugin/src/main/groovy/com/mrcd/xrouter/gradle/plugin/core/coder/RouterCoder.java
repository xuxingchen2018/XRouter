package com.mrcd.xrouter.gradle.plugin.core.coder;

import com.mrcd.xrouter.gradle.plugin.bean.ClassPath;
import com.mrcd.xrouter.gradle.plugin.bean.RouterParam;
import com.mrcd.xrouter.gradle.plugin.utils.CollectionUtils;
import com.mrcd.xrouter.gradle.plugin.utils.Constant;
import com.mrcd.xrouter.gradle.plugin.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.List;
import javax.lang.model.element.Modifier;

/**
 * 用于生成各个路由Java文件的核心类
 * 1.生成构造器
 * 2.生成属性
 * 3.生成设置参数的方法
 * 4.生成启动的方法
 */
public class RouterCoder {

    private boolean mSupportAndroidX;

    private ClassPath mClassPath;

    private ClassName mIntentArgName;

    private ClassName mSelfName;

    private ClassName mContextName;

    private ClassName mIntentWrapperName;

    private ClassName mInterceptorName;

    private ClassName mFragmentName;

    private ClassName mSupportFragmentName;

    private ClassName mAndroidXFragmentName;

    private MethodSpec mConstructor;

    private TypeSpec.Builder mClassBuilder;

    public RouterCoder(ClassPath classPath, boolean supportAndroidX) {
        mClassPath = classPath;
        mSupportAndroidX = supportAndroidX;
        initNameSpace();
        generateConstructor();
        generateField();
        generateSetter();
        generateLauncherMethod();
    }

    /**
     * 初始化命名空间
     */
    private void initNameSpace() {
        mIntentArgName = ClassName.get(Constant.LIBRARY_CORE_PKG_NAME, Constant.INTENT_ARG);
        mSelfName = ClassName.get(Coder.sRoutersPkgName, mClassPath.getRouterName() + Constant.ROUTER_SUFFIX);
        mContextName = ClassName.get(Constant.CONTEXT_PKG, Constant.CONTEXT_NAME);
        mIntentWrapperName = ClassName.get(Constant.LIBRARY_CORE_PKG_NAME, Constant.INTENT_WRAPPER_NAME);
        mInterceptorName = ClassName.get(Constant.LIBRARY_CORE_PKG_NAME, Constant.INTENT_INTERCEPTOR);
        mFragmentName = ClassName.get(Constant.FRAGMENT_PKG, Constant.FRAGMENT_NAME);
        mSupportFragmentName = ClassName.get(Constant.SUPPORT_FRAGMENT_PKG, Constant.FRAGMENT_NAME);
        mAndroidXFragmentName = ClassName.get(Constant.ANDROIDX_FRAGMENT_PKG, Constant.FRAGMENT_NAME);
    }

    private void generateConstructor() {
        mConstructor = MethodSpec.constructorBuilder()
                                 .addModifiers(Modifier.PUBLIC)
                                 .addStatement("mArgs = $T.getInstance().prepare()", mIntentWrapperName)
                                 .addStatement("mRequestCode = -1")
                                 .build();
    }

    private void generateField() {
        Modifier[] modifiers = new Modifier[]{Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL};
        FieldSpec routerPath = FieldSpec.builder(String.class, "NAME", modifiers)
            .initializer("$S", mClassPath.getClassName())
            .build();
        mClassBuilder = TypeSpec.classBuilder(mClassPath.getRouterName() + Constant.ROUTER_SUFFIX)
                                .addJavadoc(Constant.JAVA_DOC_FORMAT)
                                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                .addField(TypeName.INT, "mRequestCode", Modifier.PRIVATE)
                                .addField(mIntentArgName, "mArgs", Modifier.PRIVATE)
                                .addField(mInterceptorName, "mInterceptor", Modifier.PRIVATE)
                                .addField(routerPath)
                                .addMethod(mConstructor);
    }

    private void generateSetter() {
        MethodSpec requestCodeMethod = MethodSpec.methodBuilder("setRequestCode")
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .addParameter(TypeName.INT, "requestCode")
                                                 .returns(mSelfName)
                                                 .addStatement("mRequestCode = requestCode")
                                                 .addStatement("return this")
                                                 .build();
        mClassBuilder.addMethod(requestCodeMethod);

        List<RouterParam> params = mClassPath.getParams();
        if (CollectionUtils.isNotEmpty(params)) {
            MethodSpec setter;
            for (RouterParam param : params) {
                MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder(getSetterName(param.getParamName()))
                                                             .addModifiers(Modifier.PUBLIC)
                                                             .returns(mSelfName);
                String type = param.getType();
                if (param.isHuge()) {
                    setterBuilder.addStatement("mArgs.largeValue($S, param)", param.getParamName());
                } else {
                    setterBuilder.addStatement("mArgs.setValue($S, param)", param.getParamName());
                }
                if (param.isHuge()) {
                    setterBuilder.addParameter(Object.class, "param");
                } else if (type.contains(".")) {
                    setterBuilder.addParameter(getParamType(type), "param");
                } else {
                    setterBuilder.addParameter(Constant.CLASS_MAP.get(type), "param");
                }
                setterBuilder.addStatement("return this");
                setter = setterBuilder.build();
                mClassBuilder.addMethod(setter);
            }
        }
        MethodSpec interceptor = MethodSpec.methodBuilder("setInterceptor")
                                           .addModifiers(Modifier.PUBLIC)
                                           .addParameter(mInterceptorName, "interceptor")
                                           .returns(mSelfName)
                                           .addStatement("mInterceptor = interceptor")
                                           .addStatement("return this")
                                           .build();
        mClassBuilder.addMethod(interceptor);
    }

    private String getSetterName(String paramName) {
        if (paramName.startsWith("m")) {
            paramName = paramName.substring(1);
        }
        return "set" + StringUtils.toUpperCaseFirstOne(paramName);
    }

    private ClassName getParamType(String paramType) {
        return ClassName.bestGuess(paramType);
    }

    private void generateLauncherMethod() {
        //构建针对context的启动方法
        MethodSpec contextLauncherMethod = launcherMethod(mContextName, "context");
        mClassBuilder.addMethod(contextLauncherMethod);

        if (mSupportAndroidX) {
            //支持AndroidX的情况下，构建AndroidX下的fragment启动方法
            MethodSpec xLauncherMethod = launcherMethod(mAndroidXFragmentName, "fragment");
            mClassBuilder.addMethod(xLauncherMethod);
        } else {
            //support库下的fragment启动方法
            MethodSpec supportLauncherMethod = launcherMethod(mSupportFragmentName, "fragment");
            mClassBuilder.addMethod(supportLauncherMethod);
        }
        //android.app包下的fragment启动方法
        MethodSpec fragmentLauncherMethod = launcherMethod(mFragmentName, "fragment");
        mClassBuilder.addMethod(fragmentLauncherMethod);
    }

    private MethodSpec launcherMethod(ClassName className, String paramName) {
        CodeBlock block = CodeBlock.of("mArgs.requestCode(mRequestCode).wrap(" + paramName + ").intercept" + "(mInterceptor).launch(NAME)");

        return MethodSpec.methodBuilder("launch")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addParameter(className, paramName)
            .returns(TypeName.VOID)
            .addStatement(block.toString())
            .build();
    }

    public void build() {
        JavaFile javaFile = JavaFile.builder(Coder.sRoutersPkgName, mClassBuilder.build()).build();
        try {
            System.out.println("Start generate Router----->" + mClassPath.getSimpleName() + "::Path >> " + mClassPath.getRouterName());
            javaFile.writeTo(Constant.sJavaFileOutPutDir);
        } catch (IOException e) {
            System.out.println("Exception----------------->" + mClassPath.getSimpleName() + "::Path >> " + mClassPath.getRouterName());
            e.printStackTrace();
        }
    }

}
