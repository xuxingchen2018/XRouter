package com.mrcd.xrouter.compiler.core.coder;

/**
 * 代码生成的接口
 * 根据不同的数据类型，生成不同的从IntentWrapper取值的代码
 */
public interface ITypeCoder {

    /**
     * 生成取值的代码
     *
     * @param fieldName 属性名
     * @param key       键
     * @return 生成的代码
     */
    String generateCode(String fieldName, String key);

    /**
     * 释放IntentWrapper中的巨型数据
     *
     * @param fieldName 属性名
     * @param key       键
     * @return 只有在largeValue的时候会返回代码, 生成的代码
     */
    String releaseCode(String fieldName, String key);

}
