package com.mrcd.xrouter.core;

import android.content.Intent;
import android.os.Parcelable;
import com.mrcd.xrouter.builder.IValueBuilder;
import com.mrcd.xrouter.builder.impl.BooleanBuilder;
import com.mrcd.xrouter.builder.impl.ByteBuilder;
import com.mrcd.xrouter.builder.impl.CharBuilder;
import com.mrcd.xrouter.builder.impl.DoubleBuilder;
import com.mrcd.xrouter.builder.impl.FloatBuilder;
import com.mrcd.xrouter.builder.impl.IntBuilder;
import com.mrcd.xrouter.builder.impl.IntentStringBuilder;
import com.mrcd.xrouter.builder.impl.LongBuilder;
import com.mrcd.xrouter.builder.impl.ParcelableBuilder;
import com.mrcd.xrouter.builder.impl.SerializableBuilder;
import com.mrcd.xrouter.builder.impl.ShortBuilder;
import com.mrcd.xrouter.core.IntentArgs.Launcher;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * intent包装器
 */
public class IntentWrapper {

    private static final IntentWrapper INSTANCE = new IntentWrapper();

    private static Map<Class, IValueBuilder> sBuilders = new HashMap<>();

    static {
        sBuilders.put(short.class, new ShortBuilder());
        sBuilders.put(int.class, new IntBuilder());
        sBuilders.put(long.class, new LongBuilder());
        sBuilders.put(float.class, new FloatBuilder());
        sBuilders.put(double.class, new DoubleBuilder());
        sBuilders.put(byte.class, new ByteBuilder());
        sBuilders.put(char.class, new CharBuilder());
        sBuilders.put(boolean.class, new BooleanBuilder());
        sBuilders.put(String.class, new IntentStringBuilder());
        sBuilders.put(Serializable.class, new SerializableBuilder());
        sBuilders.put(Parcelable.class, new ParcelableBuilder());
    }

    private IntentWrapper() {

    }

    public static IntentWrapper getInstance() {
        return INSTANCE;
    }

    private Map<String, Object> mLargeValues = new HashMap<>();

    public IntentArgs prepare() {
        return new IntentArgs();
    }

    public Launcher wrap() {
        return new IntentArgs().wrap();
    }

    void setLargeValues(String key, Object value) {
        mLargeValues.put(key, value);
    }

    <C> IValueBuilder<C> getBuilder(Class<C> c) {
        return sBuilders.get(c);
    }

    public <Res> Res getLargeValue(String key) {
        try {
            return (Res) mLargeValues.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <Res> Res getLargeValue(Object key) {
        if (key != null) {
            try {
                return (Res) mLargeValues.get(key.getClass().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <Res> Res getLargeValue(Class key) {
        if (key != null) {
            try {
                return (Res) mLargeValues.get(key.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public short getShort(Intent intent, String key) {
        return getBuilder(short.class).getValue(intent, key);
    }

    public int getInt(Intent intent, String key) {
        return getBuilder(int.class).getValue(intent, key);
    }

    public float getFloat(Intent intent, String key) {
        return getBuilder(float.class).getValue(intent, key);
    }

    public long getLong(Intent intent, String key) {
        return getBuilder(long.class).getValue(intent, key);
    }

    public double getDouble(Intent intent, String key) {
        return getBuilder(double.class).getValue(intent, key);
    }

    public char getChar(Intent intent, String key) {
        return getBuilder(char.class).getValue(intent, key);
    }

    public byte getByte(Intent intent, String key) {
        return getBuilder(byte.class).getValue(intent, key);
    }

    public boolean getBoolean(Intent intent, String key) {
        return getBuilder(boolean.class).getValue(intent, key);
    }

    public String getString(Intent intent, String key) {
        return getBuilder(String.class).getValue(intent, key);
    }

    public Serializable getSerializable(Intent intent, String key) {
        return getBuilder(Serializable.class).getValue(intent, key);
    }

    public Parcelable getParcelable(Intent intent, String key) {
        return getBuilder(Parcelable.class).getValue(intent, key);
    }

    //    --------------------------------------------------------

    public short getShort(Intent intent, String key, short def) {
        return getBuilder(short.class).getValue(intent, key, def);
    }

    public int getInt(Intent intent, String key, int def) {
        return getBuilder(int.class).getValue(intent, key, def);
    }

    public float getFloat(Intent intent, String key, float def) {
        return getBuilder(float.class).getValue(intent, key, def);
    }

    public long getLong(Intent intent, String key, long def) {
        return getBuilder(long.class).getValue(intent, key, def);
    }

    public double getDouble(Intent intent, String key, double def) {
        return getBuilder(double.class).getValue(intent, key, def);
    }

    public char getChar(Intent intent, String key, char def) {
        return getBuilder(char.class).getValue(intent, key, def);
    }

    public byte getByte(Intent intent, String key, byte def) {
        return getBuilder(byte.class).getValue(intent, key, def);
    }

    public boolean getBoolean(Intent intent, String key, boolean def) {
        return getBuilder(boolean.class).getValue(intent, key, def);
    }

    public String getString(Intent intent, String key, String def) {
        return getBuilder(String.class).getValue(intent, key, def);
    }

    public Serializable getSerializable(Intent intent, String key, Serializable def) {
        return getBuilder(Serializable.class).getValue(intent, key, def);
    }

    public Parcelable getParcelable(Intent intent, String key, Parcelable def) {
        return getBuilder(Parcelable.class).getValue(intent, key, def);
    }

    public void clearLargeValue(String key) {
        mLargeValues.remove(key);
    }

    public void clearLargeValue(Object key) {
        mLargeValues.remove(key.getClass().getName());
    }

    public void clearLargeValue(Class key) {
        mLargeValues.remove(key.getName());
    }

    public void clearAll() {
        mLargeValues.clear();
    }

    public static void bindData(Object target) {
        bindData(target, target.getClass());
    }

    /**
     * 此方法专门用于继承关系时强制指定匹配的class类型
     * 注意，此处的thisClass不能通过getClass()方法获取，否则在父类中无效
     *
     * @param target    需要注入的对象
     * @param thisClass class类,尽量通过 XXX.class的方法获取
     */
    public static void bindData(Object target, Class thisClass) {
        if (null == target || Object.class == thisClass) {
            return;
        }
        DataBinder.invokeBindDataMethod(target, thisClass);
    }

    public static void release(Object target) {
        release(target, target.getClass());
    }

    public static void release(Object target, Class thisClass) {
        if (null == target || Object.class == thisClass) {
            return;
        }
        DataBinder.invokeReleaseDataMethod(target, thisClass);
    }

}
