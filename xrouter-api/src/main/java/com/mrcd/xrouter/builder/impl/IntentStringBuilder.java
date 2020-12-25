package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;

import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * string 参数构建器
 */
public class IntentStringBuilder implements IValueBuilder<String> {

    @Override
    public void setIntent(Intent intent, String key, String value) {
        if (null != intent && !TextUtils.isEmpty(key) && value != null) {
            intent.putExtra(key, value);
        }
    }

    @Override
    public String getValue(Intent intent, String key) {
        return intent.getStringExtra(key);
    }

    @Override
    public String getValue(Intent intent, String key, String defValue) {
        String value = intent.getStringExtra(key);
        if (TextUtils.isEmpty(value)) {
            return defValue;
        }
        return value;
    }
}
