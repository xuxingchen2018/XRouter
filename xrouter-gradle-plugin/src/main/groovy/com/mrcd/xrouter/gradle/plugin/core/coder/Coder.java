package com.mrcd.xrouter.gradle.plugin.core.coder;

import com.mrcd.xrouter.gradle.auto.transform.DataBinderWriter;
import com.mrcd.xrouter.gradle.plugin.bean.ClassPath;
import com.mrcd.xrouter.gradle.plugin.configs.DevelopConfig;
import com.mrcd.xrouter.gradle.plugin.core.EngineConfig;
import com.mrcd.xrouter.gradle.plugin.utils.Constant;
import com.mrcd.xrouter.gradle.plugin.utils.JsonIO;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 代码器
 */
public class Coder {

    /**
     * 路由名重复异常
     */
    private static final String REPEATED_PATH = "Repeated path:%s";

    /**
     * 开发者配置的路由表包路径
     */
    public static String sRoutersPkgName;

    /**
     * .java长度
     */
    private static final int JAVA_SUFFIX_LENGTH = ".java".length();

    /**
     * 开始生成代码
     * @param rootDir 主工程根目录
     * @param config 配置项
     */
    public static void startCoding(File rootDir, EngineConfig config) {
        DevelopConfig developConfig = config.getDevelopConfig();

        sRoutersPkgName = developConfig.getRouterPath();
        Constant.generateJavaHomeDir(rootDir, config.getProjectName());
        List<ClassPath> currentPaths = new ArrayList<>();
        List<RouterCoder> routerCoders = new ArrayList<>();
        XRouterCoder xRouterCoder = new XRouterCoder();
        for (String item : config.getProjects()) {
            File file = Constant.getJsonCacheFile(rootDir, item);
            if (file.exists()) {
                List<ClassPath> paths = JsonIO.readJsonFile(file);
                for (ClassPath path : paths) {
                    RouterCoder coder = new RouterCoder(path, developConfig.getSupportAndroidX());
                    if (currentPaths.contains(path)) {
                        throw new RuntimeException(String.format(REPEATED_PATH, path.getRouterName()));
                    }
                    currentPaths.add(path);
                    routerCoders.add(coder);
                    xRouterCoder.createRouterMethod(path);
                }
            } else {
                System.err.println(file.getAbsolutePath() + "  <<<<< File not exist");
            }
        }
        for (RouterCoder coder : routerCoders) {
            coder.build();
        }
        System.err.println("\n共处理===>> " + routerCoders.size() + " 条路由");
        xRouterCoder.build();
        destroy(config.getCachePaths(), currentPaths);
    }

    private static void destroy(List<ClassPath> cache, List<ClassPath> currentPaths) {
        List<ClassPath> cachePaths = new ArrayList<>(readRouterDir());
        Iterator<ClassPath> cacheIterator = cachePaths.iterator();
        while (cacheIterator.hasNext()) {
            //缓存的路由是否被删除了
            boolean deleted = true;
            ClassPath item = cacheIterator.next();
            for (ClassPath currentItem : currentPaths) {
                if (item.getRouterName()
                        .equals(String.format(Constant.ROUTER_NAME_FORMAT, currentItem.getRouterName()))) {
                    //如果当前的路由表中有之前所保存的路由，说明路由还是有用的，但不能说明参数没被修改
                    deleted = false;
                    break;
                }
            }
            if (deleted) {
                System.err.println("Overdue router:" + item.getRouterName());
            }
        }
    }

    private static List<ClassPath> readRouterDir() {
        List<ClassPath> paths = new ArrayList<>();
        File routerDir = new File(Constant.sJavaFileOutPutDir, sRoutersPkgName.replaceAll("\\.", File.separator));
        String[] list = routerDir.list();
        for (String name : list) {
            ClassPath path = new ClassPath();
            String routerName = name.substring(0, name.length() - JAVA_SUFFIX_LENGTH);
            path.setPath(routerName);
            paths.add(path);
        }
        return paths;
    }


}
