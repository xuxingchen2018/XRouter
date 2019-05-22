package com.mrcd.xrouter.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.mrcd.xrouter.XRouter;
import com.mrcd.xrouter.annotation.Parcelable;
import com.mrcd.xrouter.annotation.Serializable;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.annotation.XPath;
import com.mrcd.xrouter.core.IntentWrapper;
import com.mrcd.xrouter.demo.bean.Animal;
import com.mrcd.xrouter.demo.bean.Dog;
import com.mrcd.xrouter.demo.bean.User;

@XPath(path = "AppMain")
public class MainActivity extends AppCompatActivity {

    @XParam()
    int userName;

    @XParam()
    boolean a;

    @XParam()
    short b;

    @XParam()
    long c;

    @XParam()
    double d;

    @XParam()
    float e;

    @XParam()
    char f;

    @Parcelable()
    Animal mAnimal;

    @Serializable(name = "author")
    User mUser;

    @XParam()
    byte g;

    @XParam()
    String h;

    @XParam(isLarge = true)
    Dog mDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XRouter.getInstance()
               .demoActivity()
               .setFrom("MainActivity")
               .setCurrentPage("MainActivity")
               .setRequestCode(1024)
               .launch(this);
    }
}
