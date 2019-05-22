package com.mrcd.xrouter.gradle.plugin.utils;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 字符串是否为空
     *
     * @param string 字符串对象
     * @return true 空字符串  false 非空字符串
     */
    public static boolean isEmpty(String string) {
        if (null == string || "".equals(string) || string.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 首字母转小写
     *
     * @param s 字符串
     * @return 转小写后的字符串
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 判断首字母是否是小写
     *
     * @param s 字符串目标
     * @return true 首字母小写  false 首字母不是小写（注意，首字母不是小写不代表一定是大写）
     */
    public static boolean isFirstOneLowerCase(String s) {
        return !isEmpty(s) && Character.isLowerCase(s.charAt(0));
    }

    /**
     * 首字母转大写
     *
     * @param s 字符串
     * @return 首字母转大写后的字符串
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 判断首字母是否是大写
     *
     * @param s 字符串
     * @return true 首字母是大写 false 首字母不是大写
     */
    public static boolean isFirstOneUpperCase(String s) {
        return !isEmpty(s) && Character.isUpperCase(s.charAt(0));
    }

}
