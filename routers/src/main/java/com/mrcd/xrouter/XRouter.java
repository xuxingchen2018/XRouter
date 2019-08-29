package com.mrcd.xrouter;

import com.xxc.xrouter.routers.AppMainRouter;
import com.xxc.xrouter.routers.DemoActivityRouter;
import com.xxc.xrouter.routers.MainSecondActivityRouter;

public final class XRouter {
  private static XRouter INSTANCE = new XRouter();

  private XRouter() {
  }

  public static XRouter getInstance() {
    return INSTANCE;
  }

  public MainSecondActivityRouter mainSecondActivity() {
    return new MainSecondActivityRouter();
  }

  public AppMainRouter appMain() {
    return new AppMainRouter();
  }

  public DemoActivityRouter demoActivity() {
    return new DemoActivityRouter();
  }
}
