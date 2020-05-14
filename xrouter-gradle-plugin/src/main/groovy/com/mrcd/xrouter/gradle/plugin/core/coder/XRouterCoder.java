package com.mrcd.xrouter.gradle.plugin.core.coder;

import com.mrcd.xrouter.gradle.plugin.bean.ClassPath;
import com.mrcd.xrouter.gradle.plugin.utils.Constant;
import com.mrcd.xrouter.gradle.plugin.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.lang.model.element.Modifier;

import static com.mrcd.xrouter.gradle.plugin.utils.Constant.ANNOTATION_PKG_PATH;
import static com.mrcd.xrouter.gradle.plugin.utils.Constant.NAVIGATION_NAME;
import static javax.lang.model.element.Modifier.PRIVATE;

/**
 * 用于动态生成XRouter的核心类
 * 1.生成私有构造器
 * 2.静态实例
 * 3.静态方法获取实例
 * 4.创建各个activity的启动方法
 */
public class XRouterCoder {

    public static final String XROUTER_PKG_PATH = "com.mrcd.xrouter";

    private ClassName mSelfName;

    private ClassName mInterceptorName;

    private TypeSpec.Builder mClassBuilder;

    private String mLauncherName;

    public XRouterCoder(String launcherName) {
        mLauncherName = launcherName;
        if (StringUtils.isEmpty(mLauncherName)) {
            mLauncherName = Constant.XROUTER_NAME;
        }
        initNameSpace();
        generateConstructor();
        generateField();
        generateSetter();
    }

    private void initNameSpace() {
        mSelfName = ClassName.get(XROUTER_PKG_PATH, mLauncherName);
        mInterceptorName = ClassName.get(Constant.LIBRARY_CORE_PKG_NAME, Constant.INTENT_INTERCEPTOR);
        ClassName navigation = ClassName.get(ANNOTATION_PKG_PATH, NAVIGATION_NAME);
        mClassBuilder = TypeSpec.classBuilder(mLauncherName)
                .addJavadoc(Constant.JAVA_DOC_FORMAT)
                .addAnnotation(navigation)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    private void generateConstructor() {
        Modifier[] modifiers = new Modifier[]{PRIVATE, Modifier.STATIC};
        FieldSpec fieldSpec = FieldSpec.builder(mSelfName, "INSTANCE", modifiers)
                                       .initializer("new $T()", mSelfName)
                                       .build();
        MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(PRIVATE).build();
        MethodSpec staticGet = MethodSpec.methodBuilder("getInstance")
                                         .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                         .returns(mSelfName)
                                         .addStatement("return INSTANCE")
                                         .build();
        mClassBuilder.addField(fieldSpec).addMethod(constructor).addMethod(staticGet);
    }

    private void generateField() {
        String fieldName = "mGlobalInterceptor";
        FieldSpec interceptor = FieldSpec.builder(mInterceptorName, fieldName, PRIVATE).build();
        mClassBuilder.addField(interceptor);
    }

    private void generateSetter() {
        MethodSpec interceptor = MethodSpec.methodBuilder("setGlobalInterceptor")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(mInterceptorName, "interceptor")
            .returns(TypeName.VOID)
            .addStatement("mGlobalInterceptor = interceptor")
            .build();
        mClassBuilder.addMethod(interceptor);
    }

    void createRouterMethod(ClassPath classPath) {
        String routerName = classPath.getRouterName();
        String methodName = StringUtils.toLowerCaseFirstOne(routerName);
        ClassName returnName = ClassName.get(Coder.sRoutersPkgName, routerName + Constant.ROUTER_SUFFIX);
        MethodSpec methodSpec = MethodSpec.methodBuilder(methodName)
                                          .addModifiers(Modifier.PUBLIC)
                                          .returns(returnName)
            .addStatement("return new $T().setInterceptor(mGlobalInterceptor)", returnName)
                                          .build();
        mClassBuilder.addMethod(methodSpec);
    }


    void build() {
        JavaFile javaFile = JavaFile.builder(XROUTER_PKG_PATH, mClassBuilder.build()).build();
        try {
            javaFile.writeTo(Constant.sJavaFileOutPutDir);
        } catch (IOException e) {
            System.out.println("异常-----------------> " + mLauncherName);
            e.printStackTrace();
        }
    }

}
