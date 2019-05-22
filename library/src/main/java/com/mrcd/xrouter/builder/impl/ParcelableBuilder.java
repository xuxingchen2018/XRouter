package com.mrcd.xrouter.builder.impl;

import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;

/**
 * Parcelable参数构建器
 */
public class ParcelableBuilder implements IValueBuilder<Parcelable> {

    @Override
    public void setIntent(Intent intent, String key, Parcelable value) {
        if (intent != null && !TextUtils.isEmpty(key) && null != value) {
            intent.putExtra(key, value);
        }
    }

    @Override
    public Parcelable getValue(Intent intent, String key) {
        return intent.getParcelableExtra(key);
    }

    @Override
    public Parcelable getValue(Intent intent, String key, Parcelable defValue) {
        Parcelable parcelable = intent.getParcelableExtra(key);
        if (parcelable == null) {
            return defValue;
        }
        return parcelable;
    }
}
