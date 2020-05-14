package com.mrcd.xrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 源码级别的注解，用于注解生成的路由管理类，即XRouter
 * 主要用于AS插件扫描
 * AS插件扫描到被Navigation注解的XRouter类以后，会遍历所有的方法，并获取返回值，
 * 通过返回值的class类型中的NAME属性，找到与之对应的Activity类，从而达到随点随跳的效果
 * 详见XRouter-Navigation插件
 *
 * @link https://github.com/SevenNight2012/XRouter-Navigator
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Navigation {

}
