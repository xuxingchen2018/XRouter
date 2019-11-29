package com.mrcd.xrouter.gradle.plugin.core.task

import com.mrcd.xrouter.gradle.plugin.bean.ClassPath
import com.mrcd.xrouter.gradle.plugin.configs.DevelopConfig
import com.mrcd.xrouter.gradle.plugin.core.EngineConfig
import com.mrcd.xrouter.gradle.plugin.core.coder.Coder
import com.mrcd.xrouter.gradle.plugin.utils.Constant
import com.mrcd.xrouter.gradle.plugin.utils.JsonIO
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 自定义task
 * 最原始的办法，通过gradle调用命令的方式，执行assemble方法，
 * 耗时长，且容易出错，每人的本地运行环境不同，执行命令经常出现问题，且需要配置相关的环境变量
 */
class MakeRoutersTask extends DefaultTask {

    List<String> mProjects

    @TaskAction
    void makeRouters() {
        mProjects.each { name ->
            System.err.println("参与构建的module有 : $name")
        }
        File rootDir = project.rootProject.projectDir

        List<ClassPath> cachePaths = new ArrayList<>()
        DevelopConfig config = project.XRouterConfig

        mProjects.removeAll(config.excludeProject)
        mProjects.each { itemProject ->
            File jsonFile = Constant.getJsonCacheFile(rootDir, itemProject)
            if (jsonFile.exists()) {
                println "Read cache json >>> " + jsonFile
                cachePaths.addAll(JsonIO.readJsonFile(jsonFile))
            }
        }
        EngineConfig engineConfig = new EngineConfig()
        engineConfig.projects = mProjects
        engineConfig.cachePaths = cachePaths
        engineConfig.projectName = project.name
        engineConfig.developConfig = config
        Coder.startCoding(rootDir, engineConfig)
    }

}
