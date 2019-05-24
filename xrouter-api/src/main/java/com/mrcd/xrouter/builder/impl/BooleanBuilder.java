package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * boolean参数的构建器
 */
public class BooleanBuilder implements IValueBuilder<Boolean> {

    @Override
    public void setIntent(Intent intent, String key, Boolean value) {
        if (intent != null && null != value && !TextUtils.isEmpty(key)) {
            intent.putExtra(key, (boolean) value);
        }
    }

    @Override
    public Boolean getValue(Intent intent, String key) {
        return intent.getBooleanExtra(key, false);
    }

    @Override
    public Boolean getValue(Intent intent, String key, Boolean defValue) {
        return intent.getBooleanExtra(key, defValue);
    }
}
