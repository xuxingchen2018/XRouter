package com.xxc.xrouter.routers;

import android.content.Context;
import android.os.Parcelable;
import com.mrcd.xrouter.core.IntentArgs;
import com.mrcd.xrouter.core.IntentInterceptor;
import com.mrcd.xrouter.core.IntentWrapper;
import java.io.Serializable;
import java.lang.Object;
import java.lang.String;

public final class AppMainRouter {
  private int mRequestCode;

  private IntentArgs mArgs;

  private IntentInterceptor mInterceptor;

  public AppMainRouter() {
    mArgs = IntentWrapper.getInstance().prepare();
    mRequestCode = -1;
  }

  public AppMainRouter setRequestCode(int requestCode) {
    mRequestCode = requestCode;
    return this;
  }

  public AppMainRouter setUserName(int param) {
    mArgs.setValue("userName", param);
    return this;
  }

  public AppMainRouter setA(boolean param) {
    mArgs.setValue("a", param);
    return this;
  }

  public AppMainRouter setB(short param) {
    mArgs.setValue("b", param);
    return this;
  }

  public AppMainRouter setC(long param) {
    mArgs.setValue("c", param);
    return this;
  }

  public AppMainRouter setD(double param) {
    mArgs.setValue("d", param);
    return this;
  }

  public AppMainRouter setE(float param) {
    mArgs.setValue("e", param);
    return this;
  }

  public AppMainRouter setF(char param) {
    mArgs.setValue("f", param);
    return this;
  }

  public AppMainRouter setAnimal(Parcelable param) {
    mArgs.setValue("mAnimal", param);
    return this;
  }

  public AppMainRouter setAuthor(Serializable param) {
    mArgs.setValue("author", param);
    return this;
  }

  public AppMainRouter setG(byte param) {
    mArgs.setValue("g", param);
    return this;
  }

  public AppMainRouter setH(String param) {
    mArgs.setValue("h", param);
    return this;
  }

  public AppMainRouter setDog(Object param) {
    mArgs.largeValue("mDog", param);
    return this;
  }

  public AppMainRouter setInterceptor(IntentInterceptor interceptor) {
    mInterceptor = interceptor;
    return this;
  }

  public final void launch(Context context) {
    mArgs.requestCode(mRequestCode).wrap().intercept(mInterceptor).launch(context, "com.mrcd.xrouter.demo.MainActivity");
  }
}
