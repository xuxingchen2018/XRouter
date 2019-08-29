package com.mrcd.xrouter.gradle.plugin.configs
/**
 * 路由构建器的配置
 */
class DevelopConfig {

    /**
     * 如果配置了exclude项目的话，那么不会扫描该项目下的路由缓存文件，也不会生成改工程下的路由表
     */
    List<String> excludeProject = new ArrayList<>()

    /**
     * 构建类型，此处不再增加variants细分
     */
    List<String> buildTypes = new ArrayList<>(["Debug", "Release"])

    /**
     * 路由表的输出路径
     */
    String routerPath = "com.mrcd.xrouter.routers"

    DevelopConfig() {
    }

    List<String> getExcludeProject() {
        return excludeProject
    }

    void setExcludeProject(List<String> excludeProject) {
        this.excludeProject = excludeProject
    }

    String getRouterPath() {
        return routerPath
    }

    void setRouterPath(String routerPath) {
        this.routerPath = routerPath
    }

    List<String> getBuildTypes() {
        return buildTypes
    }

    void setBuildTypes(List<String> buildTypes) {
        this.buildTypes = buildTypes
    }
}
