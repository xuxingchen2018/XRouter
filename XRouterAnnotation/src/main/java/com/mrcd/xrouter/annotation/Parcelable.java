package com.mrcd.xrouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解实现了Parcelable接口的变量
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parcelable {


    DataType type() default DataType.PARCELABLE;

    String name() default "";

}
