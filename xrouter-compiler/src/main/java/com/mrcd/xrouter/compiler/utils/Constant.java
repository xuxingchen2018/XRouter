package com.mrcd.xrouter.compiler.utils;

/**
 * 常量，主要记录了Activity，intent等一些常用类的包名，类名
 */
public class Constant {

    /**
     * android.content包路径
     */
    public static final String PKG_ANDROID_CONTENT = "android.content";

    /**
     * library中的核心包路径
     */
    public static final String CORE_PKG_NAME = "com.mrcd.xrouter.core";

    /**
     * intent类名
     */
    public static final String INTENT = "Intent";

    /**
     * IntentWrapper的类名
     */
    public static final String INTENT_WRAPPER = "IntentWrapper";

    /**
     * 生成的数据注入类的后缀
     */
    public static final String DATA_BINDER_SUFFIX = "DataBinder";

    /**
     * module名的key，根据key可以取到module名
     */
    public static final String KEY_MODULE_NAME = "MODULE_NAME";

}
