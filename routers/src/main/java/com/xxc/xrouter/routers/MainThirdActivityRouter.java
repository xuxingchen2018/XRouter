package com.xxc.xrouter.routers;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.mrcd.xrouter.core.IntentArgs;
import com.mrcd.xrouter.core.IntentInterceptor;
import com.mrcd.xrouter.core.IntentWrapper;
import java.lang.String;

public final class MainThirdActivityRouter {
  private int mRequestCode;

  private IntentArgs mArgs;

  private IntentInterceptor mInterceptor;

  public MainThirdActivityRouter() {
    mArgs = IntentWrapper.getInstance().prepare();
    mRequestCode = -1;
  }

  public MainThirdActivityRouter setRequestCode(int requestCode) {
    mRequestCode = requestCode;
    return this;
  }

  public MainThirdActivityRouter setAge(int param) {
    mArgs.setValue("mAge", param);
    return this;
  }

  public MainThirdActivityRouter setName(String param) {
    mArgs.setValue("name", param);
    return this;
  }

  public MainThirdActivityRouter setGreet(String param) {
    mArgs.setValue("greet", param);
    return this;
  }

  public MainThirdActivityRouter setInterceptor(IntentInterceptor interceptor) {
    mInterceptor = interceptor;
    return this;
  }

  public final void launch(Context context) {
    mArgs.requestCode(mRequestCode).wrap(context).intercept(mInterceptor).launch("com.mrcd.xrouter.demo.third.MainThirdActivity");
  }

  public final void launch(Fragment fragment) {
    mArgs.requestCode(mRequestCode).wrap(fragment).intercept(mInterceptor).launch("com.mrcd.xrouter.demo.third.MainThirdActivity");
  }

  public final void launch(android.app.Fragment fragment) {
    mArgs.requestCode(mRequestCode).wrap(fragment).intercept(mInterceptor).launch("com.mrcd.xrouter.demo.third.MainThirdActivity");
  }
}
