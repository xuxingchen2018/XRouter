package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * long 参数构建器
 */
public class LongBuilder implements IValueBuilder<Long> {

    @Override
    public void setIntent(Intent intent, String key, Long value) {
        if (intent != null && null != value && !TextUtils.isEmpty(key)) {
            intent.putExtra(key, (long) value);
        }
    }

    @Override
    public Long getValue(Intent intent, String key) {
        return intent.getLongExtra(key, 0);
    }

    @Override
    public Long getValue(Intent intent, String key, Long defValue) {
        return intent.getLongExtra(key, defValue);
    }
}
