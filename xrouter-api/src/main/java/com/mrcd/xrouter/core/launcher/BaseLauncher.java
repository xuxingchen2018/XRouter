package com.mrcd.xrouter.core.launcher;

import android.content.Intent;
import com.mrcd.xrouter.core.IntentInterceptor;

/**
 * launcher 基类
 */
class BaseLauncher {

    protected Intent mIntent;
    protected int mRequestCode;
    protected IntentInterceptor mInterceptor;

    public BaseLauncher(Intent intent, int requestCode) {
        mIntent = intent;
        mRequestCode = requestCode;
    }
}
