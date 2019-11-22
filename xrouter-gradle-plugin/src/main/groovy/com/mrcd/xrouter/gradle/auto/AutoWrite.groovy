package com.mrcd.xrouter.gradle.auto

import com.android.build.gradle.AppExtension
import com.mrcd.xrouter.gradle.auto.transform.AutoWriteTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 自动注入绑定数据和解绑数据代码的插件
 */
class AutoWrite implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getExtensions().getByType(AppExtension).registerTransform(new AutoWriteTransform(project))
    }
}
