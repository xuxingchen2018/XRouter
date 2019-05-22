package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * double参数构建器
 */
public class DoubleBuilder implements IValueBuilder<Double> {

    @Override
    public void setIntent(Intent intent, String key, Double value) {
        if (intent != null && null != value && !TextUtils.isEmpty(key)) {
            intent.putExtra(key, (double) value);
        }
    }

    @Override
    public Double getValue(Intent intent, String key) {
        return intent.getDoubleExtra(key, 0);
    }

    @Override
    public Double getValue(Intent intent, String key, Double defValue) {
        return intent.getDoubleExtra(key, defValue);
    }
}
