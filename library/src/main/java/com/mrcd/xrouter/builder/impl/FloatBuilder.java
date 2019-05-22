package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * float参数构建器
 */
public class FloatBuilder implements IValueBuilder<Float> {

    @Override
    public void setIntent(Intent intent, String key, Float value) {
        if (intent != null && null != value && !TextUtils.isEmpty(key)) {
            intent.putExtra(key, (float) value);
        }
    }

    @Override
    public Float getValue(Intent intent, String key) {
        return intent.getFloatExtra(key, 0);
    }

    @Override
    public Float getValue(Intent intent, String key, Float defValue) {
        return intent.getFloatExtra(key, defValue);
    }
}
