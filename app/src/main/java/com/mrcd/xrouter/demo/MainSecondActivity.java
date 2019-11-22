package com.mrcd.xrouter.demo;

import android.support.v7.app.AppCompatActivity;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.annotation.XPath;

@XPath
public class MainSecondActivity extends AppCompatActivity {

    public static final String TAG = "MainSecondActivity";

    @XParam
    public int mAge;
    @XParam
    public String name;

}
