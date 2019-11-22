package com.mrcd.xrouter.gradle.auto.transform

import com.android.build.api.transform.JarInput
import com.android.build.gradle.AppExtension
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.io.IOUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.regex.Pattern
import java.util.zip.ZipEntry

class DataBinderWriter {

    private static final ClassPool POOL = ClassPool.getDefault()
    private static final String BUNDLE_PATH = "android.os.Bundle"
    private static final String INTENT_WRAPPER_PATH = "com.mrcd.xrouter.core.IntentWrapper"
    private static final String ON_CREATE_METHOD_NAME = "onCreate"
    private static final String ON_DESTROY_METHOD_NAME = "onDestroy"

    static String sApplicationId

    private CtClass mBundleClass
    private String mPathR
    public File mFileR

    DataBinderWriter(Project project) {
        AppExtension app = project.extensions.getByType(AppExtension)
        sApplicationId = app.defaultConfig.applicationId
        //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        POOL.appendClassPath(project.android.bootClasspath[0].toString())
        POOL.importPackage(BUNDLE_PATH)

        String dir = sApplicationId.replaceAll("\\.", File.separator)
        println "应用applicationId >> " + sApplicationId
        mPathR = dir + File.separator + "R.class"
        println "R文件 >> " + mPathR
    }

    void appendClassPath(String path, boolean hasIntentWrapper) {
        POOL.appendClassPath(path)
        if (hasIntentWrapper) {
            POOL.importPackage(INTENT_WRAPPER_PATH)
        }
    }

    void injectClass(File dir, Project project) {
        String root = dir.absolutePath + File.separator
        System.err.println(root)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                if (filterFile(file) && file.isFile()) {
                    CtClass ctClass
                    if ((ctClass = isNeedInjectClass(file.absolutePath.replace(root, ""))) != null) {
                        injectCode(ctClass)
                        ctClass.writeFile(dir.absolutePath)
                        ctClass.detach()
                        System.err.println(file.absolutePath)
                    }
                }
            }
        }
    }

    void injectJar(File src, File dest) {
        if (null == mBundleClass) {
            mBundleClass = POOL.get(BUNDLE_PATH)
        }
        POOL.appendClassPath(src.absolutePath)
        def optJar = new File(src.getParent(), src.name + ".opt")
        if (optJar.exists())
            optJar.delete()

        def jar = new JarFile(src)
        Enumeration enumeration = jar.entries()
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(optJar))

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.name
            ZipEntry zipEntry = new ZipEntry(entryName)
            InputStream inputStream = jar.getInputStream(jarEntry)
            jarOutputStream.putNextEntry(zipEntry)

            CtClass ctClass
            if (entryName.endsWith(".class") && (ctClass = isNeedInjectClass(entryName)) != null) {
                injectCode(ctClass)
                def bytes = ctClass.toBytecode()
                jarOutputStream.write(bytes)
                ctClass.detach()
            } else {
                jarOutputStream.write(IOUtils.toByteArray(inputStream))
            }
            inputStream.close()
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        jar.close()
        if (src.exists()) {
            src.delete()
        }
        optJar.renameTo(src)
    }

    private void injectCode(CtClass ctClass) {
        boolean hasOverrideCreate = ctClass.declaredMethods.any { method ->
            CtClass[] bundleParam = [mBundleClass]
            return method.name == ON_CREATE_METHOD_NAME && bundleParam == method.parameterTypes
        }
        boolean hasOverrideDestroy = ctClass.declaredMethods.any { method ->
            return method.name == ON_DESTROY_METHOD_NAME
        }
        System.err.println("$ctClass.name 重写onCreate >>> " + hasOverrideCreate + "  重写onDestroy >>> " + hasOverrideDestroy)
        CtMethod onCreateMethod
        if (hasOverrideCreate) {
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
        CtMethod onDestroyMethod
        if (hasOverrideDestroy) {
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

    private CtClass isNeedInjectClass(String entryName) {
        String className = entryName.replaceAll(".class", "").replaceAll(File.separator, ".")
        try {
            CtClass ctClass = POOL.get(className)
            if (null != ctClass && ctClass.hasAnnotation("com.mrcd.xrouter.annotation.XPath")) {
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
     * 判定该jar包是否需要扫描
     * @param jarInput
     * @return
     */
    boolean shouldScanJar(JarInput jarInput, Project project) {
        return project.rootProject.childProjects.values().any { child ->
            return jarInput.name == ":${child.name}"
        }
    }

    /**
     * 过滤文件
     * 除去R文件，以及support库下的文件
     * @param file 文件对象
     * @return 是否符合要求
     */
    boolean filterFile(File file) {
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
            if (path.endsWith(mPathR)) {
                mFileR = file
                String R = mPathR.substring(0, mPathR.length() - 6)
                println "R >>>>>>>>>>>>>>>>>>>>>>>>>> " + R
                POOL.importPackage(R.replaceAll(File.separator, "."))
            }
            return false
        }
        Pattern androidR$ = Pattern.compile("^.*R\\\$.*\\.class.*\$")
        if (androidR$.matcher(path).matches()) {
            return false
        }
        return true
    }

}
