package com.xxc.xrouter.routers;

import android.content.Context;
import android.os.Parcelable;
import com.mrcd.xrouter.core.IntentArgs;
import com.mrcd.xrouter.core.IntentInterceptor;
import com.mrcd.xrouter.core.IntentWrapper;
import java.io.Serializable;
import java.lang.Object;
import java.lang.String;

public final class MainActivityRouter {
  private int mRequestCode;

  private IntentArgs mArgs;

  private IntentInterceptor mInterceptor;

  public MainActivityRouter() {
    mArgs = IntentWrapper.getInstance().prepare();
    mRequestCode = -1;
  }

  public MainActivityRouter setRequestCode(int requestCode) {
    mRequestCode = requestCode;
    return this;
  }

  public MainActivityRouter setUserName(int param) {
    mArgs.setValue("userName", param);
    return this;
  }

  public MainActivityRouter setA(boolean param) {
    mArgs.setValue("a", param);
    return this;
  }

  public MainActivityRouter setB(short param) {
    mArgs.setValue("b", param);
    return this;
  }

  public MainActivityRouter setC(long param) {
    mArgs.setValue("c", param);
    return this;
  }

  public MainActivityRouter setD(double param) {
    mArgs.setValue("d", param);
    return this;
  }

  public MainActivityRouter setE(float param) {
    mArgs.setValue("e", param);
    return this;
  }

  public MainActivityRouter setF(char param) {
    mArgs.setValue("f", param);
    return this;
  }

  public MainActivityRouter setG(byte param) {
    mArgs.setValue("g", param);
    return this;
  }

  public MainActivityRouter setH(String param) {
    mArgs.setValue("h", param);
    return this;
  }

  public MainActivityRouter setAnimal(Parcelable param) {
    mArgs.setValue("mAnimal", param);
    return this;
  }

  public MainActivityRouter setAuthor(Serializable param) {
    mArgs.setValue("author", param);
    return this;
  }

  public MainActivityRouter setDog(Object param) {
    mArgs.largeValue("mDog", param);
    return this;
  }

  public MainActivityRouter setInterceptor(IntentInterceptor interceptor) {
    mInterceptor = interceptor;
    return this;
  }

  public final void launch(Context context) {
    mArgs.requestCode(mRequestCode).wrap().intercept(mInterceptor).launch(context, "com.mrcd.xrouter.demo.MainActivity");
  }
}
