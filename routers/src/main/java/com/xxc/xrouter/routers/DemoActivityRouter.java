package com.xxc.xrouter.routers;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.mrcd.xrouter.core.IntentArgs;
import com.mrcd.xrouter.core.IntentInterceptor;
import com.mrcd.xrouter.core.IntentWrapper;
import java.lang.String;

public final class DemoActivityRouter {
  private int mRequestCode;

  private IntentArgs mArgs;

  private IntentInterceptor mInterceptor;

  public DemoActivityRouter() {
    mArgs = IntentWrapper.getInstance().prepare();
    mRequestCode = -1;
  }

  public DemoActivityRouter setRequestCode(int requestCode) {
    mRequestCode = requestCode;
    return this;
  }

  public DemoActivityRouter setFrom(String param) {
    mArgs.setValue("mFrom", param);
    return this;
  }

  public DemoActivityRouter setCurrentPage(String param) {
    mArgs.setValue("mCurrentPage", param);
    return this;
  }

  public DemoActivityRouter setInterceptor(IntentInterceptor interceptor) {
    mInterceptor = interceptor;
    return this;
  }

  public final void launch(Context context) {
    mArgs.requestCode(mRequestCode).wrap(context).intercept(mInterceptor).launch("com.mrcd.demo.DemoActivity");
  }

  public final void launch(Fragment fragment) {
    mArgs.requestCode(mRequestCode).wrap(fragment).intercept(mInterceptor).launch("com.mrcd.demo.DemoActivity");
  }

  public final void launch(android.app.Fragment fragment) {
    mArgs.requestCode(mRequestCode).wrap(fragment).intercept(mInterceptor).launch("com.mrcd.demo.DemoActivity");
  }
}
