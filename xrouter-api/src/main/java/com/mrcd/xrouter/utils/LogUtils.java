package com.mrcd.xrouter.utils;

import android.util.Log;

public class LogUtils {

    public static final String TAG = "XRouterLog";

    public static boolean sLog = true;

    public static void d(String msg) {
        if (sLog) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (sLog) {
            Log.e(TAG, msg);
        }
    }

}
