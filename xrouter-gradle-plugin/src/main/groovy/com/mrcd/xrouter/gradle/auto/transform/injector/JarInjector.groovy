package com.mrcd.xrouter.gradle.auto.transform.injector

import javassist.ClassPool
import javassist.CtClass
import org.apache.commons.io.IOUtils

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * 专门针对jar包的代码注入，将会遍历Jar包中的文件
 */
class JarInjector extends BaseCodeInjector {


    JarInjector(ClassPool pool) {
        super(pool)
    }

    @Override
    void doInject(File src) {
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
            if (entryName.endsWith(".class") && (ctClass = getTarget(entryName)) != null) {
                injectMethodCreate(ctClass)
                injectMethodDestroy(ctClass)

                System.err.println("class >> $ctClass.name  重写 create>$mOverrideCreate  重写 destroy>$mOverrideDestroy")
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
}
