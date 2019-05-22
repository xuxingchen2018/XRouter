package com.mrcd.xrouter.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.annotation.XPath;

@XPath
public class MainSecondActivity extends AppCompatActivity {

    @XParam
    int mAge;
    @XParam
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
