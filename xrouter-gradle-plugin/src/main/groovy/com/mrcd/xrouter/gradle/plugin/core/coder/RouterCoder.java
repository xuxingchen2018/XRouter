package com.mrcd.xrouter.gradle.plugin.core.coder;

import com.mrcd.xrouter.gradle.plugin.bean.ClassPath;
import com.mrcd.xrouter.gradle.plugin.bean.RouterParam;
import com.mrcd.xrouter.gradle.plugin.utils.CollectionUtils;
import com.mrcd.xrouter.gradle.plugin.utils.Constant;
import com.mrcd.xrouter.gradle.plugin.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
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

    private ClassPath mClassPath;

    private ClassName mIntentArgName;

    private ClassName mSelfName;

    private ClassName mContextName;

    private ClassName mIntentWrapperName;

    private ClassName mInterceptorName;

    private MethodSpec mConstructor;

    private TypeSpec.Builder mClassBuilder;

    public RouterCoder(ClassPath classPath) {
        mClassPath = classPath;
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
    }

    private void generateConstructor() {
        mConstructor = MethodSpec.constructorBuilder()
                                 .addModifiers(Modifier.PUBLIC)
                                 .addStatement("mArgs = $T.getInstance().prepare()", mIntentWrapperName)
                                 .addStatement("mRequestCode = -1")
                                 .build();
    }

    private void generateField() {
        mClassBuilder = TypeSpec.classBuilder(mClassPath.getRouterName() + Constant.ROUTER_SUFFIX)
                                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                .addField(TypeName.INT, "mRequestCode", Modifier.PRIVATE)
                                .addField(mIntentArgName, "mArgs", Modifier.PRIVATE)
                                .addField(mInterceptorName, "mInterceptor", Modifier.PRIVATE)
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
        CodeBlock block = CodeBlock.of("mArgs.requestCode(mRequestCode).wrap().intercept(mInterceptor).launch(context, $S)", mClassPath
            .getClassName());
        MethodSpec launch = MethodSpec.methodBuilder("launch")
                                      .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                      .addParameter(mContextName, "context")
                                      .returns(TypeName.VOID)
                                      .addStatement(block.toString())
                                      .build();
        mClassBuilder.addMethod(launch);
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
