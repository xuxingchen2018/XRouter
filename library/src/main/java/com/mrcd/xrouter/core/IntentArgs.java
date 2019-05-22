package com.mrcd.xrouter.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import com.mrcd.xrouter.builder.IValueBuilder;
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

    public Launcher wrap() {
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
        return new Launcher(intent, mRequestCode);
    }

    /**
     * intent启动器
     */
    public class Launcher {

        private Intent mIntent;
        private int mRequestCode;
        private IntentInterceptor mInterceptor;

        public Launcher(Intent intent, int requestCode) {
            mIntent = intent;
            mRequestCode = requestCode;
        }

        public Launcher intercept(IntentInterceptor interceptor) {
            mInterceptor = interceptor;
            return this;
        }

        public void launch(Context context, Class target) {
            mIntent.setComponent(new ComponentName(context, target));
            launch(context);
        }

        public void launch(Context context, String target) {
            ComponentName name = new ComponentName(context.getPackageName(), target);
            mIntent.setComponent(name);
            launch(context);
        }

        private void launch(Context context) {
            if (mInterceptor != null && mInterceptor.intercept(mIntent)) {
                return;
            }
            if (!(context instanceof Activity)) {
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            try {
                if (-1 != mRequestCode && context instanceof Activity) {
                    ((Activity) context).startActivityForResult(mIntent, mRequestCode);
                } else {
                    context.startActivity(mIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
