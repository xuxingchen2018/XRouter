package com.mrcd.xrouter.core.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.mrcd.xrouter.core.IntentInterceptor;

/**
 * context launcher
 */
public class ContextLauncher extends BaseLauncher {

    private Context mContext;

    public ContextLauncher(Context context, Intent intent, int requestCode) {
        super(intent, requestCode);
        mContext = context;
    }

    public ContextLauncher intercept(IntentInterceptor interceptor) {
        mInterceptor = interceptor;
        return this;
    }

    public void launch(Class target) {
        mIntent.setComponent(new ComponentName(mContext, target));
        launch();
    }

    public void launch(String target) {
        ComponentName name = new ComponentName(mContext.getPackageName(), target);
        mIntent.setComponent(name);
        launch();
    }

    private void launch() {
        if (mInterceptor != null && mInterceptor.intercept(mIntent)) {
            return;
        }
        if (!(mContext instanceof Activity)) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        try {
            if (-1 != mRequestCode && mContext instanceof Activity) {
                ((Activity) mContext).startActivityForResult(mIntent, mRequestCode);
            } else {
                mContext.startActivity(mIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
