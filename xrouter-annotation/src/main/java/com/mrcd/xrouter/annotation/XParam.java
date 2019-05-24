package com.mrcd.xrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参数的注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XParam {

    DataType type() default DataType.NORMAL;

    /**
     * 参数名字，如果没有设置，那么会直接取在代码中定义的名字
     *
     * @return 参数名字符串
     */
    String name() default "";

    /**
     * 是否是大型数据，如果是大型数据，那么会以静态变量的形式存储在IntentWrap中
     *
     * @return true 是大型数据，false 不是大型数据
     */
    boolean isLarge() default false;

}
