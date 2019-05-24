package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * int参数构建器
 */
public class IntBuilder implements IValueBuilder<Integer> {

    @Override
    public void setIntent(Intent intent, String key, Integer value) {
        if (intent != null && null != value && !TextUtils.isEmpty(key)) {
            intent.putExtra(key, (int) value);
        }
    }

    @Override
    public Integer getValue(Intent intent, String key) {
        return intent.getIntExtra(key, 0);
    }

    @Override
    public Integer getValue(Intent intent, String key, Integer defValue) {
        return intent.getIntExtra(key,defValue);
    }
}
