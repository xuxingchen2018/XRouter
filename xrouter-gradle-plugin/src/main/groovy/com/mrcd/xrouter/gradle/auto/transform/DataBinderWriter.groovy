package com.mrcd.xrouter.gradle.auto.transform

import com.android.build.api.transform.JarInput
import com.android.build.gradle.AppExtension
import com.mrcd.xrouter.gradle.auto.transform.injector.DirectoryInjector
import com.mrcd.xrouter.gradle.auto.transform.injector.JarInjector
import javassist.ClassPool
import org.gradle.api.Project

class DataBinderWriter {

    public static final String BUNDLE_PATH = "android.os.Bundle"

    public static final List<String> LOGS = new ArrayList<>()
    private static final ClassPool POOL = ClassPool.getDefault()
    private static final String INTENT_WRAPPER_PATH = "com.mrcd.xrouter.core.IntentWrapper"

    static String sApplicationId

    private String mPathR

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

    def appendClassPath = { String path, boolean hasIntentWrapper ->
        POOL.appendClassPath(path)
        if (hasIntentWrapper) {
            POOL.importPackage(INTENT_WRAPPER_PATH)
        }
    }

    def processDirectoryClass = { File dir ->
        CodeInjector injector = new DirectoryInjector(POOL)
        injector.doInject(dir)
    }

    def processJarClass = { File jar ->
        CodeInjector injector = new JarInjector(POOL)
        injector.doInject(jar)
    }

    /**
     * 判定该jar包是否需要扫描
     * @param jarInput jar包
     * @return true 代表library的jar，false，三方依赖的jar
     */
    static boolean shouldScanJar(JarInput jarInput, Project project) {
        return project.rootProject.childProjects.values().any { child ->
            return jarInput.name == ":${child.name}"
        }
    }

}
