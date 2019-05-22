package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * short 参数构建器
 */
public class ShortBuilder implements IValueBuilder<Short> {

    @Override
    public void setIntent(Intent intent, String key, Short value) {
        if (intent != null && null != value && !TextUtils.isEmpty(key)) {
            intent.putExtra(key, (short) value);
        }
    }

    @Override
    public Short getValue(Intent intent, String key) {
        return intent.getShortExtra(key, (short) 0);
    }

    @Override
    public Short getValue(Intent intent, String key, Short defValue) {
        return intent.getShortExtra(key, defValue);
    }
}
