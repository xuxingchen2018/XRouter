package com.mrcd.xrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解实现了Serializable接口的变量
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Serializable {

    DataType type() default DataType.SERIALIZABLE;

    String name() default "";

}
