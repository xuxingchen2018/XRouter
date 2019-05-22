package com.mrcd.xrouter.gradle.plugin.core;

import com.mrcd.xrouter.gradle.plugin.bean.ClassPath;
import com.mrcd.xrouter.gradle.plugin.configs.DevelopConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * 引擎配置
 */
public class EngineConfig {

    /**
     * 配置了插件的工程名字
     */
    private String mProjectName;

    /**
     * 所有需要生成路由表的工程名字
     */
    private List<String> mProjects = new ArrayList<>();

    /**
     * 开发者的配置
     */
    private DevelopConfig mDevelopConfig;

    /**
     * 上次缓存的路由表
     */
    private List<ClassPath> mCachePaths = new ArrayList<>();

    public String getProjectName() {
        return mProjectName;
    }

    public void setProjectName(String projectName) {
        mProjectName = projectName;
    }

    public List<String> getProjects() {
        return mProjects;
    }

    public void setProjects(List<String> projects) {
        mProjects = projects;
    }

    public DevelopConfig getDevelopConfig() {
        return mDevelopConfig;
    }

    public void setDevelopConfig(DevelopConfig developConfig) {
        mDevelopConfig = developConfig;
    }

    public List<ClassPath> getCachePaths() {
        return mCachePaths;
    }

    public void setCachePaths(List<ClassPath> cachePaths) {
        mCachePaths = cachePaths;
    }
}
