package com.mrcd.xrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取路径的方法
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XPath {

    /**
     * 返回路径
     *
     * @return 路径字符串
     */
    String path() default "";

}
