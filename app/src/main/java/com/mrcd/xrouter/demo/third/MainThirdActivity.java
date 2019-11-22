package com.mrcd.xrouter.demo.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.annotation.XPath;
import com.mrcd.xrouter.demo.MainSecondActivity;
import com.mrcd.xrouter.demo.R;

@XPath
public class MainThirdActivity extends MainSecondActivity {

    @XParam
    public String greet;

    private TextView mTextHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mTextHint = findViewById(R.id.text_hint);
        String text = mAge + "  " + name + "  " + greet;
        mTextHint.setText(text);
    }
}
