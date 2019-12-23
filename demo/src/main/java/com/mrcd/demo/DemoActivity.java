package com.mrcd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.annotation.XPath;
import java.util.Locale;

@XPath()
public class DemoActivity extends AppCompatActivity {

    private static final String HINT_FORMAT = "come from %s,this is page %s";

    @XParam
    String mFrom;
    @XParam
    String mCurrentPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        TextView text = findViewById(R.id.demo_text);
        text.setText(String.format(Locale.US, HINT_FORMAT, mFrom, mCurrentPage));
    }
}
