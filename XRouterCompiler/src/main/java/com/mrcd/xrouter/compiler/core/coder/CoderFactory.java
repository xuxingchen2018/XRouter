package com.mrcd.xrouter.compiler.core.coder;

import com.mrcd.xrouter.compiler.core.coder.impl.BooleanCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.ByteCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.CharCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.DoubleCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.FloatCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.IntCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.LargeCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.LongCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.ParcelableCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.SerializableCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.ShortCoder;
import com.mrcd.xrouter.compiler.core.coder.impl.StringCoder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CoderFactory {

    private static final String LARGE_MODEL = "LargeModel";

    private static final CoderFactory INSTANCE = new CoderFactory();

    public static CoderFactory getInstance() {
        return INSTANCE;
    }

    private Map<String, ITypeCoder> mCoderMap = new HashMap<>();

    private CoderFactory() {
        mCoderMap.put(short.class.getName(), new ShortCoder());
        mCoderMap.put(int.class.getName(), new IntCoder());
        mCoderMap.put(boolean.class.getName(), new BooleanCoder());
        mCoderMap.put(byte.class.getName(), new ByteCoder());
        mCoderMap.put(char.class.getName(), new CharCoder());
        mCoderMap.put(double.class.getName(), new DoubleCoder());
        mCoderMap.put(float.class.getName(), new FloatCoder());
        mCoderMap.put(long.class.getName(), new LongCoder());
        mCoderMap.put(String.class.getName(), new StringCoder());
        mCoderMap.put("android.os.Parcelable", new ParcelableCoder());
        mCoderMap.put(Serializable.class.getName(), new SerializableCoder());
        mCoderMap.put(LARGE_MODEL, new LargeCoder());
    }

    public ITypeCoder getCoder(String fieldType) {
        return mCoderMap.get(fieldType);
    }

    public ITypeCoder getParcelableCoder() {
        return mCoderMap.get("android.os.Parcelable");
    }

    public ITypeCoder getSerializableCoder() {
        return mCoderMap.get(Serializable.class.getName());
    }

    public ITypeCoder getLargeCoder() {
        return mCoderMap.get(LARGE_MODEL);
    }
}
