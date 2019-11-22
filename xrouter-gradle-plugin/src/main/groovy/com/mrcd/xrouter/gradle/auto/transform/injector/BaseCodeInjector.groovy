package com.mrcd.xrouter.gradle.auto.transform.injector

import com.mrcd.xrouter.gradle.auto.transform.CodeInjector
import com.mrcd.xrouter.gradle.auto.transform.DataBinderWriter
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod

import java.util.regex.Pattern

/**
 * 代码注入器基类
 */
abstract class BaseCodeInjector implements CodeInjector {

    protected static final String ANNOTATION_XPATH = "com.mrcd.xrouter.annotation.XPath"
    private static final String ON_CREATE_METHOD_NAME = "onCreate"
    private static final String ON_DESTROY_METHOD_NAME = "onDestroy"

    protected ClassPool mPool
    protected CtClass mBundleClass
    protected boolean mOverrideCreate
    protected boolean mOverrideDestroy

    BaseCodeInjector(ClassPool pool) {
        mPool = pool
        mBundleClass = mPool.get(DataBinderWriter.BUNDLE_PATH)
    }

    /**
     * 向onCreate方法中注入代码
     */
    protected void injectMethodCreate(CtClass ctClass) {
        mOverrideCreate = ctClass.declaredMethods.any { method ->
            CtClass[] bundleParam = [mBundleClass]
            return method.name == ON_CREATE_METHOD_NAME && bundleParam == method.parameterTypes
        }
        CtMethod onCreateMethod
        if (mOverrideCreate) {
            //如果重写了onCreate方法，那么直接在第一行注入即可
            onCreateMethod = ctClass.getDeclaredMethod(ON_CREATE_METHOD_NAME, mBundleClass)
            onCreateMethod.insertBefore("{\n" +
                    "            IntentWrapper.bindData(this);\n" +
                    "        }")
        } else {
            //如果没有重写，那么需要添加onCreate方法，然后在第一行注入，并且执行super语句
            CtClass[] params = [mBundleClass]
            onCreateMethod = new CtMethod(CtClass.voidType, ON_CREATE_METHOD_NAME, params, ctClass)
            onCreateMethod.setBody("{\n" +
                    "            IntentWrapper.bindData(this);\n" +
                    "            super.onCreate(\$1);\n" +
                    "        }")
            ctClass.addMethod(onCreateMethod)
        }
    }

    /**
     * 向onDestroy方法中注入代码
     */
    protected void injectMethodDestroy(CtClass ctClass) {
        mOverrideDestroy = ctClass.declaredMethods.any { method ->
            return method.name == ON_DESTROY_METHOD_NAME
        }
        CtMethod onDestroyMethod
        if (mOverrideDestroy) {
            onDestroyMethod = ctClass.getDeclaredMethod(ON_DESTROY_METHOD_NAME, null)
            onDestroyMethod.insertAfter("{\n" +
                    "            IntentWrapper.release(this);\n" +
                    "        }")
        } else {
            onDestroyMethod = new CtMethod(CtClass.voidType, ON_DESTROY_METHOD_NAME, null, ctClass)
            onDestroyMethod.setBody("{\n" +
                    "            super.onDestroy();\n" +
                    "            IntentWrapper.release(this);\n" +
                    "        }")
            ctClass.addMethod(onDestroyMethod)
        }
    }

    /**
     * 根据类文件路径，加载CtClass对象
     * @param classPath 类文件路径
     * @return CtClass对象
     */
    protected CtClass getTarget(String classPath) {
        String className = classPath.replaceAll(".class", "").replaceAll(File.separator, ".")
        try {
            CtClass ctClass = mPool.get(className)
            if (null != ctClass && ctClass.hasAnnotation(ANNOTATION_XPATH)) {
                return ctClass
            } else {
                return null
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 过滤文件
     * 除去R文件，以及support库下的文件
     * @param file 文件对象
     * @return 是否符合要求
     */
    protected boolean filterFile(File file) {
        String path = file.absolutePath
        Pattern androidSupport = Pattern.compile("android/support.*")
        if (androidSupport.matcher(path).matches()) {
            return false
        }
        Pattern androidX = Pattern.compile("androidx.*")
        if (androidX.matcher(path).matches()) {
            return false
        }
        Pattern androidR = Pattern.compile("^.*R\\.class.*\$")
        if (androidR.matcher(path).matches()) {
//            if (path.endsWith(mPathR)) {
//                String R = mPathR.substring(0, mPathR.length() - 6)
//                println "R >>>>>>>>>>>>>>>>>>>>>>>>>> " + R
//                mPathR.importPackage(R.replaceAll(File.separator, "."))
//            }
            return false
        }
        Pattern androidR$ = Pattern.compile("^.*R\\\$.*\\.class.*\$")
        if (androidR$.matcher(path).matches()) {
            return false
        }
        return true
    }
}
