package com.mrcd.xrouter.gradle.plugin.core

import com.mrcd.xrouter.gradle.plugin.bean.ClassPath
import com.mrcd.xrouter.gradle.plugin.configs.DevelopConfig
import com.mrcd.xrouter.gradle.plugin.core.coder.Coder
import com.mrcd.xrouter.gradle.plugin.utils.Constant
import com.mrcd.xrouter.gradle.plugin.utils.GradleUtils
import com.mrcd.xrouter.gradle.plugin.utils.JsonIO
import org.apache.http.util.TextUtils
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.process.ExecResult
import org.gradle.process.ExecSpec

/**
 * 核心工作，主要定义了核心的task
 * 获取开发者配置
 * 读取缓存路由
 * 运行task任务
 * 动态生成代码，先生成路由表，后生成XRouter
 */
class CoreWork implements IEngineWork {

    @Override
    void startEngine(Project project, Task task, File rootDir) {
        String taskNameFormat = ":%s:assemble"
        List<String> args = new ArrayList<>()
        List<String> projects = new ArrayList<>()
        List<ClassPath> cachePaths = new ArrayList<>()
        def output = new ByteArrayOutputStream()
        DevelopConfig config
        ExecResult execResult

        //获取主工程目录下的APP工程
        project.rootProject.childProjects.values().each { childProject ->
            if (GradleUtils.isAndroidProject(childProject)) {
                projects.add(childProject.name)
                String taskName = String.format(taskNameFormat, childProject.name)
                args.add(taskName)
                println "Task任务:  " + taskName

            }
        }

        //收集开发者的配置
        task.doFirst {
            config = project.XRouterConfig
            projects.removeAll(config.excludeProject)
            projects.each { itemProject ->
                File jsonFile = Constant.getJsonCacheFile(rootDir, itemProject)
                if (jsonFile.exists()) {
                    println "读取缓存配置 >>> " + jsonFile
                    cachePaths.addAll(JsonIO.readJsonFile(jsonFile))
                }
            }

            execResult = project.rootProject.exec(new Action<ExecSpec>() {
                @Override
                void execute(ExecSpec execSpec) {
                    execSpec.executable = "gradlew"
                    execSpec.args = args
                    execSpec.errorOutput = output
                }
            })
        }
        task.doLast {
            String errMsg = output.toString()
            if (!TextUtils.isEmpty(errMsg) && errMsg.contains(Constant.EXCEPTION_KEY)) {
                System.err.println(errMsg)
            } else {
                EngineConfig engineConfig = new EngineConfig()
                engineConfig.projects = projects
                engineConfig.cachePaths = cachePaths
                engineConfig.projectName = project.name
                engineConfig.developConfig = config

                Coder.startCoding(rootDir, engineConfig)
            }
        }

    }
}