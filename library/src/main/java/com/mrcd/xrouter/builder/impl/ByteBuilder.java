package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * byte参数构建器
 */
public class ByteBuilder implements IValueBuilder<Byte> {

    @Override
    public void setIntent(Intent intent, String key, Byte value) {
        if (intent != null && null != value && !TextUtils.isEmpty(key)) {
            intent.putExtra(key, (byte) value);
        }
    }

    @Override
    public Byte getValue(Intent intent, String key) {
        return intent.getByteExtra(key, (byte) 0);
    }

    @Override
    public Byte getValue(Intent intent, String key, Byte defValue) {
        return intent.getByteExtra(key, defValue);
    }
}
