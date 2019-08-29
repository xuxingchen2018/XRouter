package com.mrcd.xrouter.gradle.plugin.core.task

import com.mrcd.xrouter.gradle.plugin.bean.ClassPath
import com.mrcd.xrouter.gradle.plugin.configs.DevelopConfig
import com.mrcd.xrouter.gradle.plugin.core.EngineConfig
import com.mrcd.xrouter.gradle.plugin.core.coder.Coder
import com.mrcd.xrouter.gradle.plugin.utils.Constant
import com.mrcd.xrouter.gradle.plugin.utils.GradleUtils
import com.mrcd.xrouter.gradle.plugin.utils.JsonIO
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 自定义task
 * 最原始的办法，通过gradle调用命令的方式，执行assemble方法，
 * 耗时长，且容易出错，每人的本地运行环境不同，执行命令经常出现问题，且需要配置相关的环境变量
 */
class MakeRoutersTask extends DefaultTask {

    File mRootDir

    @TaskAction
    void makeRouters() {
        List<String> projects = new ArrayList<>()
        List<ClassPath> cachePaths = new ArrayList<>()
        DevelopConfig config = project.XRouterConfig

        //获取主工程目录下的APP工程
        project.rootProject.childProjects.values().each { childProject ->
            if (GradleUtils.isAndroidProject(childProject)) {
                projects.add(childProject.name)
            }
        }

        projects.removeAll(config.excludeProject)
        projects.each { itemProject ->
            File jsonFile = Constant.getJsonCacheFile(mRootDir, itemProject)
            if (jsonFile.exists()) {
                println "Read cache json >>> " + jsonFile
                cachePaths.addAll(JsonIO.readJsonFile(jsonFile))
            }
        }
        EngineConfig engineConfig = new EngineConfig()
        engineConfig.projects = projects
        engineConfig.cachePaths = cachePaths
        engineConfig.projectName = project.name
        engineConfig.developConfig = config
        Coder.startCoding(mRootDir, engineConfig)
    }

}
