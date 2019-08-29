package com.mrcd.xrouter.gradle.plugin.core.coder;

import com.mrcd.xrouter.gradle.plugin.bean.ClassPath;
import com.mrcd.xrouter.gradle.plugin.utils.Constant;
import com.mrcd.xrouter.gradle.plugin.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import javax.lang.model.element.Modifier;

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

    private TypeSpec.Builder mClassBuilder;

    public XRouterCoder() {
        initNameSpace();
        generateConstructor();
    }

    private void initNameSpace() {
        mSelfName = ClassName.get(XROUTER_PKG_PATH, Constant.XROUTER_NAME);

        mClassBuilder = TypeSpec.classBuilder(Constant.XROUTER_NAME).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    private void generateConstructor() {
        Modifier[] modifiers = new Modifier[]{Modifier.PRIVATE, Modifier.STATIC};
        FieldSpec fieldSpec = FieldSpec.builder(mSelfName, "INSTANCE", modifiers)
                                       .initializer("new $T()", mSelfName)
                                       .build();
        MethodSpec constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
        MethodSpec staticGet = MethodSpec.methodBuilder("getInstance")
                                         .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                                         .returns(mSelfName)
                                         .addStatement("return INSTANCE")
                                         .build();
        mClassBuilder.addField(fieldSpec).addMethod(constructor).addMethod(staticGet);
    }

    public void createRouterMethod(ClassPath classPath) {
        String routerName = classPath.getRouterName();
        String methodName = StringUtils.toLowerCaseFirstOne(routerName);
        ClassName returnName = ClassName.get(Coder.sRoutersPkgName, routerName + Constant.ROUTER_SUFFIX);
        MethodSpec methodSpec = MethodSpec.methodBuilder(methodName)
                                          .addModifiers(Modifier.PUBLIC)
                                          .returns(returnName)
                                          .addStatement("return new $T()", returnName)
                                          .build();
        mClassBuilder.addMethod(methodSpec);
    }


    public void build() {
        JavaFile javaFile = JavaFile.builder(XROUTER_PKG_PATH, mClassBuilder.build()).build();
        try {
            javaFile.writeTo(Constant.sJavaFileOutPutDir);
        } catch (IOException e) {
            System.out.println("异常-----------------> XRouter");
            e.printStackTrace();
        }
    }

}
