package com.mrcd.xrouter;

import com.xxc.xrouter.routers.AppMainRouter;
import com.xxc.xrouter.routers.DemoActivityRouter;
import com.xxc.xrouter.routers.MainSecondActivityRouter;
import com.xxc.xrouter.routers.MainThirdActivityRouter;

public final class XRouter {
  private static XRouter INSTANCE = new XRouter();

  private XRouter() {
  }

  public static XRouter getInstance() {
    return INSTANCE;
  }

  public DemoActivityRouter demoActivity() {
    return new DemoActivityRouter();
  }

  public MainThirdActivityRouter mainThirdActivity() {
    return new MainThirdActivityRouter();
  }

  public MainSecondActivityRouter mainSecondActivity() {
    return new MainSecondActivityRouter();
  }

  public AppMainRouter appMain() {
    return new AppMainRouter();
  }
}
