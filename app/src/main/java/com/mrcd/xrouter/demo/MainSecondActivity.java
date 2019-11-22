package com.mrcd.xrouter.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.annotation.XPath;
import com.mrcd.xrouter.core.IntentWrapper;

@XPath
public class MainSecondActivity extends AppCompatActivity {

    public static final String TAG = "MainSecondActivity";

    @XParam
    public int mAge;
    @XParam
    public String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + mAge + "   " + name);
    }
}
