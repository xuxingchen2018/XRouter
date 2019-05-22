package com.mrcd.xrouter.compiler.utils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * 编译期时获取element包名，类名的工具类
 */
public class ProcessUtils {

    /**
     * 获取包名的方法
     *
     * @param environment 编译环境
     * @param element     元素
     * @return 包名
     */
    public static String getPackage(ProcessingEnvironment environment, Element element) {
        return environment.getElementUtils().getPackageOf(element).getQualifiedName().toString();
    }

    /**
     * 获取类名，不包含包名
     *
     * @param element 元素
     * @return 类名
     */
    public static String getSimpleClassName(Element element) {
        return element.getSimpleName().toString();
    }

    /**
     * 获取完整类路径的方法
     *
     * @param environment 环境
     * @param element     元素
     * @return 完整类路径
     */
    public static String getClassName(ProcessingEnvironment environment, Element element) {
        return getPackage(environment, element) + "." + getSimpleClassName(element);
    }

}
