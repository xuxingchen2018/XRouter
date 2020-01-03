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

    /**
     * 是否支持AndroidX
     * 自1.2.0开始，androidx的支持将通过不同的artifactId做区分
     */
    @Deprecated
    boolean supportAndroidX = false

    /**
     * application项目名称
     * 如果不指定，插件会自动扫描第一个application项目，一般情况下不会有问题，如果一个工程中存在多个application的话需要强制指定
     */
    String appModule = ""

    /**
     * 自定义路由启动器的名称，即"XRouter"
     * 开发者可以自定义一个类名，
     * eg：AppRouter，使用时就是AppRouter.getInstance().mainActivity().launch(context);
     */
    String customRouterLauncher = "XRouter"

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

    @Deprecated
    boolean getSupportAndroidX() {
        return supportAndroidX
    }

    @Deprecated
    void setSupportAndroidX(boolean supportAndroidX) {
        this.supportAndroidX = supportAndroidX
    }

    String getAppModule() {
        return appModule
    }

    void setAppModule(String appModule) {
        this.appModule = appModule
    }

    String getCustomRouterLauncher() {
        return customRouterLauncher
    }

    void setCustomRouterLauncher(String customRouterLauncher) {
        this.customRouterLauncher = customRouterLauncher
    }
}
