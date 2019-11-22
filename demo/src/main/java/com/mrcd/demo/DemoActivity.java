package com.mrcd.demo;

import android.support.v7.app.AppCompatActivity;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.annotation.XPath;

@XPath()
public class DemoActivity extends AppCompatActivity {

    @XParam
    String mFrom;
    @XParam
    String mCurrentPage;

}
