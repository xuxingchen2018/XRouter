package com.mrcd.xrouter.core;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;
import com.mrcd.xrouter.core.launcher.ContextLauncher;
import com.mrcd.xrouter.core.launcher.FragmentLauncher;
import com.mrcd.xrouter.core.launcher.FragmentSupportLauncher;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * intent参数
 */
public class IntentArgs {

    /**
     * 请求码的初始值
     */
    public static final int INITIAL_REQUEST_CODE = -1;

    private Map<String, InnerArgs> mParams = new HashMap<>();

    private int mRequestCode = INITIAL_REQUEST_CODE;

    public IntentArgs setValue(String key, int value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(int.class));
        return this;
    }

    public IntentArgs setValue(String key, short value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(short.class));
        return this;
    }

    public IntentArgs setValue(String key, float value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(float.class));
        return this;
    }

    public IntentArgs setValue(String key, double value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(double.class));
        return this;
    }

    public IntentArgs setValue(String key, long value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(long.class));
        return this;
    }

    public IntentArgs setValue(String key, char value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(char.class));
        return this;
    }

    public IntentArgs setValue(String key, byte value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(byte.class));
        return this;
    }

    public IntentArgs setValue(String key, boolean value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(boolean.class));
        return this;
    }

    public IntentArgs setValue(String key, String value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(String.class));
        return this;
    }

    public IntentArgs setValue(String key, Serializable value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(Serializable.class));
        return this;
    }

    public IntentArgs setValue(String key, Parcelable value) {
        mParams.put(key, new InnerArgs().setValue(value).setClass(Parcelable.class));
        return this;
    }

    public IntentArgs largeValue(String key, Object object) {
        if (!TextUtils.isEmpty(key) && null != object) {
            IntentWrapper.getInstance().setLargeValues(key, object);
        }
        return this;
    }

    public IntentArgs largeValue(Object object, Object value) {
        if (null != object && null != value) {
            IntentWrapper.getInstance().setLargeValues(object.getClass().getName(), value);
        }
        return this;
    }

    public IntentArgs largeValue(Class c, Object value) {
        if (c != null && null != value) {
            IntentWrapper.getInstance().setLargeValues(c.getName(), value);
        }
        return this;
    }

    public IntentArgs requestCode(int requestCode) {
        mRequestCode = requestCode;
        return this;
    }

    private Intent setupIntent() {
        Intent intent = new Intent();
        if (mParams.size() > 0) {
            Set<String> keySet = mParams.keySet();
            for (String key : keySet) {
                InnerArgs value = mParams.get(key);
                if (value != null) {
                    IValueBuilder builder = IntentWrapper.getInstance().getBuilder(value.mClass);
                    if (builder != null) {
                        builder.setIntent(intent, key, value.mValue);
                    }
                }
            }
        }
        return intent;
    }

    public ContextLauncher wrap(Context context) {
        Intent intent = setupIntent();
        return new ContextLauncher(context, intent, mRequestCode);
    }

    public FragmentSupportLauncher wrap(Fragment fragment) {
        Intent intent = setupIntent();
        return new FragmentSupportLauncher(fragment, intent, mRequestCode);
    }

    public FragmentLauncher wrap(android.app.Fragment fragment) {
        Intent intent = setupIntent();
        return new FragmentLauncher(fragment, intent, mRequestCode);
    }

    /**
     * 内部数据封装类
     */
    private static class InnerArgs {

        private Class mClass;
        private Object mValue;

        public InnerArgs setClass(Class aClass) {
            mClass = aClass;
            return this;
        }

        public InnerArgs setValue(Object value) {
            mValue = value;
            return this;
        }
    }

}
