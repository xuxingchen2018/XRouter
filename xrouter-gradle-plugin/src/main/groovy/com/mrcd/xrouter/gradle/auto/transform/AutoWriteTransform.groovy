package com.mrcd.xrouter.gradle.auto.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

/**
 * 自定义transform
 */
class AutoWriteTransform extends Transform {

    Project mProject

    AutoWriteTransform(Project project) {
        mProject = project
    }

    @Override
    String getName() {
        return getClass().getSimpleName()
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        DataBinderWriter dataWriter = new DataBinderWriter(mProject)

        def outputProvider = transformInvocation.outputProvider
        //TODO 优化
        transformInvocation.inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                dataWriter.appendClassPath(directoryInput.file.absolutePath, directoryInput.name.contains(":xrouter-api"))
            }
            input.jarInputs.each { JarInput jarInput ->
                dataWriter.appendClassPath(jarInput.file.absolutePath, jarInput.name.contains(":xrouter-api"))
            }
        }
        transformInvocation.inputs.each { TransformInput input ->
            //directory input
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //在此注入代码
                dataWriter.injectClass(directoryInput.file, mProject)
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            //jar input
            input.jarInputs.each { JarInput jarInput ->
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                File dest = outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                if (dataWriter.shouldScanJar(jarInput, mProject)) {
                    dataWriter.injectJar(jarInput.file, dest)
                }
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }
}
