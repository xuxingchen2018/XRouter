package com.mrcd.xrouter.core.launcher;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.mrcd.xrouter.core.IntentInterceptor;

/**
 * fragment Launcher
 */
public class FragmentLauncher extends BaseLauncher {

    private Fragment mFragment;

    public FragmentLauncher(Fragment fragment, Intent intent, int requestCode) {
        super(intent, requestCode);
        mFragment = fragment;
    }

    public FragmentLauncher intercept(IntentInterceptor interceptor) {
        mInterceptor = interceptor;
        return this;
    }

    public void launch(String target) {
        Context context = mFragment.getActivity();
        if (context != null) {
            mIntent.setComponent(new ComponentName(context, target));
            launch();
        }
    }

    public void launch(Class target) {
        Context context = mFragment.getActivity();
        if (context != null) {
            mIntent.setComponent(new ComponentName(context, target));
            launch();
        }
    }

    private void launch() {
        if (mInterceptor != null && mInterceptor.intercept(mIntent)) {
            return;
        }
        try {
            if (-1 != mRequestCode) {
                mFragment.startActivityForResult(mIntent, mRequestCode);
            } else {
                mFragment.startActivity(mIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
