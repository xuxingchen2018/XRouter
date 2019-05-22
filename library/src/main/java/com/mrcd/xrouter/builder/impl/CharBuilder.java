package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * char参数构建器
 */
public class CharBuilder implements IValueBuilder<Character> {

    @Override
    public void setIntent(Intent intent, String key, Character value) {
        if (intent != null && null != value && !TextUtils.isEmpty(key)) {
            intent.putExtra(key, (char) value);
        }
    }

    @Override
    public Character getValue(Intent intent, String key) {
        return intent.getCharExtra(key, (char) 0);
    }

    @Override
    public Character getValue(Intent intent, String key, Character defValue) {
        return intent.getCharExtra(key, defValue);
    }
}
