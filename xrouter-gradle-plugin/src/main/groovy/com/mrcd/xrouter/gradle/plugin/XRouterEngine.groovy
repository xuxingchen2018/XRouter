package com.mrcd.xrouter.gradle.plugin

import com.mrcd.xrouter.gradle.plugin.configs.DevelopConfig
import com.mrcd.xrouter.gradle.plugin.core.task.MakeRoutersTask
import com.mrcd.xrouter.gradle.plugin.utils.Constant
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * XRouter Gradle插件的入口类
 */
class XRouterEngine implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create(Constant.ROUTER_CONFIG, DevelopConfig.class)

        project.afterEvaluate {
            DevelopConfig config = project.XRouterConfig
            config.buildTypes.each { buildType ->
                String taskName = "make-" + buildType + "-routers"
                String dependsTaskName = "compile" + buildType + "JavaWithJavac"
                Task makeRouters = project.tasks.create(taskName, MakeRoutersTask.class, {
                    mRootDir = project.rootProject.projectDir
                })
                makeRouters.group = Constant.XROUTER_NAME
                makeRouters.description = Constant.MAKE_ROUTERS_TASK_DESCRIPTION
                makeRouters.dependsOn = [dependsTaskName]
            }
        }

    }
}