package com.mrcd.xrouter.gradle.plugin

import com.mrcd.xrouter.gradle.plugin.configs.DevelopConfig
import com.mrcd.xrouter.gradle.plugin.core.CoreWork
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
        project.rootProject.gradle.beforeProject {

        }
        Task makeRouters = project.task("makeRouters")
        makeRouters.group = Constant.XROUTER_NAME
        makeRouters.description = Constant.MAKE_ROUTERS_TASK_DESCRIPTION

        project.extensions.create(Constant.ROUTER_CONFIG, DevelopConfig.class)

        CoreWork worker = new CoreWork()
        worker.startEngine(project, makeRouters, project.rootProject.projectDir)
    }
}