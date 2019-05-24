package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;
import java.io.Serializable;

/**
 * Serializable参数构建器
 */
public class SerializableBuilder implements IValueBuilder<Serializable> {

    @Override
    public void setIntent(Intent intent, String key, Serializable value) {
        if (intent != null && !TextUtils.isEmpty(key) && null != value) {
            intent.putExtra(key, value);
        }
    }

    @Override
    public Serializable getValue(Intent intent, String key) {
        return intent.getSerializableExtra(key);
    }

    @Override
    public Serializable getValue(Intent intent, String key, Serializable defValue) {
        Serializable serializable = intent.getSerializableExtra(key);
        if (null == serializable) {
            return defValue;
        }
        return serializable;
    }
}
