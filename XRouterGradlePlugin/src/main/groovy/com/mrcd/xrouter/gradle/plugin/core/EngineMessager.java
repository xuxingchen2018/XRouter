package com.mrcd.xrouter.gradle.plugin.core;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class EngineMessager {

    private static final EngineMessager INSTANCE = new EngineMessager();

    private EngineMessager() {
        mDebugMessager = System.out;
        mErrorMessager = System.err;
    }

    public static EngineMessager getInstance() {
        return INSTANCE;
    }

    private PrintStream mDebugMessager;
    private PrintStream mErrorMessager;

    private List<String> mDebugMsg = new ArrayList<>();
    private List<String> mErrorMsg = new ArrayList<>();

    public void debug(String message) {
        mDebugMsg.add(message);
    }

    public void err(String message) {
        mErrorMsg.add(message);
    }

    public void flush() {
        for (String msg : mDebugMsg) {
            mDebugMessager.println(msg);
        }
        for (String msg : mErrorMsg) {
            mErrorMessager.println(msg);
        }
    }


}
