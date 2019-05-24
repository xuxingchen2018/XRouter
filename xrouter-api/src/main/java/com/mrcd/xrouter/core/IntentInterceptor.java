package com.mrcd.xrouter.core;

import android.content.Intent;

/**
 * intent跳转拦截器
 */
public interface IntentInterceptor {

    /**
     * 拦截回调方法
     *
     * @param intent 将要跳转的intent对象
     * @return true 拦截  false 不拦截
     */
    boolean intercept(Intent intent);

}
