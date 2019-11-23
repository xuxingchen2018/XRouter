package com.mrcd.xrouter.gradle.auto.transform.injector

import javassist.ClassPool
import javassist.CtClass

/**
 * 专门针对目录的代码注入
 */
class DirectoryInjector extends BaseCodeInjector {

    private String mRoot

    DirectoryInjector(ClassPool pool) {
        super(pool)
    }

    @Override
    void doInject(File dir) {
        mRoot = dir.absolutePath + File.separator
        dir.eachFileRecurse { File file ->
            if (filterFile(file) && file.isFile()) {
                CtClass ctClass
                if ((ctClass = getTarget(getClassPath(file))) != null) {
                    injectMethodCreate(ctClass)
                    injectMethodDestroy(ctClass)

                    ctClass.writeFile(dir.absolutePath)
                    ctClass.detach()
                    print(ctClass)
                }
            }
        }
    }

    private String getClassPath(File classFile) {
        return classFile.absolutePath.replace(mRoot, "")
    }
}
