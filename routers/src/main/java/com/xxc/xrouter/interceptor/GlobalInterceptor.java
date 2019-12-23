package com.xxc.xrouter.interceptor;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import com.mrcd.xrouter.core.IntentInterceptor;

/**
 * 全局拦截器
 */
public class GlobalInterceptor implements IntentInterceptor {

    public static final String TAG = "GlobalInterceptor";

    @Override
    public boolean intercept(Intent intent) {
        ComponentName component = intent.getComponent();
        if (component != null) {
            String className = component.getClassName();
            Log.d(TAG, "intercept: " + className);
        }
        return false;
    }
}
