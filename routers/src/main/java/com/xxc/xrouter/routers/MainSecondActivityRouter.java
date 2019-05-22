package com.xxc.xrouter.routers;

import android.content.Context;
import com.mrcd.xrouter.core.IntentArgs;
import com.mrcd.xrouter.core.IntentInterceptor;
import com.mrcd.xrouter.core.IntentWrapper;
import java.lang.String;

public final class MainSecondActivityRouter {
  private int mRequestCode;

  private IntentArgs mArgs;

  private IntentInterceptor mInterceptor;

  public MainSecondActivityRouter() {
    mArgs = IntentWrapper.getInstance().prepare();
    mRequestCode = -1;
  }

  public MainSecondActivityRouter setRequestCode(int requestCode) {
    mRequestCode = requestCode;
    return this;
  }

  public MainSecondActivityRouter setAge(int param) {
    mArgs.setValue("mAge", param);
    return this;
  }

  public MainSecondActivityRouter setName(String param) {
    mArgs.setValue("name", param);
    return this;
  }

  public MainSecondActivityRouter setInterceptor(IntentInterceptor interceptor) {
    mInterceptor = interceptor;
    return this;
  }

  public final void launch(Context context) {
    mArgs.requestCode(mRequestCode).wrap().intercept(mInterceptor).launch(context, "com.mrcd.xrouter.demo.MainSecondActivity");
  }
}
