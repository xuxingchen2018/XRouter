package com.mrcd.xrouter.compiler.exception;

/**
 * 编译期，封装的自定义异常，方便在gradle插件中判定异常类型，如果自己抛出的异常，那么会立即停止生成代码的任务
 */
public class XRouterException extends RuntimeException {

    public XRouterException(String errorMsg) {
        super(errorMsg);
    }
}
