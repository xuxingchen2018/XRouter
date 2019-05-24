package com.mrcd.xrouter.gradle.plugin.utils

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.mrcd.xrouter.gradle.plugin.configs.ItemProjectConfig
import org.apache.http.util.TextUtils
import org.gradle.api.Project

class GradleUtils {


    static boolean isAndroidProject(Project project) {
        return isApp(project) || isLibrary(project)
    }

    static boolean isApp(Project project) {
        return project.plugins.withType(AppPlugin)
    }

    static boolean isLibrary(Project project) {
        return project.plugins.withType(LibraryPlugin)
    }

    static String getAnnotationDep(String libraryVersion, ItemProjectConfig config) {
        if (config.useAnnotation) {
            String version = TextUtils.isEmpty(libraryVersion) ? '1.0.0' : libraryVersion
            return "com.mrcd:xrouter-annotation:${version}"
        }
        return ""
    }

    static String getCompilerDep(String libraryVersion, ItemProjectConfig config) {
        if (config.useCompiler) {
            String version = TextUtils.isEmpty(libraryVersion) ? '1.0.0' : libraryVersion
            return "com.mrcd:xrouter-compiler:${version}"
        }
        return ""
    }

    static String getLibraryDep(String libraryVersion, ItemProjectConfig config) {
        if (config.useLibrary) {
            String version = TextUtils.isEmpty(libraryVersion) ? '1.0.0' : libraryVersion
            return "com.mrcd:xrouter-api:${version}"
        }
        return ""
    }

}