package com.mrcd.xrouter.gradle.plugin.utils;

import java.util.Collection;

/**
 * 集合工具类
 */
public class CollectionUtils {

    /**
     * 是否为空
     *
     * @param collection 集合对象
     * @return true 空集合  false 非空集合
     */
    public static boolean isEmpty(Collection collection) {
        if (null == collection || collection.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 非空集合判空
     *
     * @param collection 集合对象
     * @return true 非空集合  false 空集合
     */
    public static boolean isNotEmpty(Collection collection) {
        return !isEmpty(collection);
    }

}
