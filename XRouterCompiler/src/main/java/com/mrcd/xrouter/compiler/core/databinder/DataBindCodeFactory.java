package com.mrcd.xrouter.compiler.core.databinder;

import com.mrcd.xrouter.annotation.Parcelable;
import com.mrcd.xrouter.annotation.Serializable;
import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.compiler.bean.RouterParam;
import com.mrcd.xrouter.compiler.core.databinder.impl.ParcelableDataBinder;
import com.mrcd.xrouter.compiler.core.databinder.impl.SerializableDataBinder;
import com.mrcd.xrouter.compiler.core.databinder.impl.XParamDataBinder;
import com.squareup.javapoet.MethodSpec;
import java.util.HashMap;
import java.util.Map;
import javax.lang.model.element.Element;

public class DataBindCodeFactory {

    private static final DataBindCodeFactory INSTANCE = new DataBindCodeFactory();

    public static DataBindCodeFactory getInstance() {
        return INSTANCE;
    }

    private DataBindCodeFactory() {
        mCoders.put(XParam.class, new XParamDataBinder());
        mCoders.put(Parcelable.class, new ParcelableDataBinder());
        mCoders.put(Serializable.class, new SerializableDataBinder());
    }

    private Map<Class, IDataBinder> mCoders = new HashMap<>();


    public RouterParam generate(MethodSpec.Builder creator, MethodSpec.Builder release, Element item) {
        RouterParam routerParam = null;
        XParam param = item.getAnnotation(XParam.class);
        if (null != param) {
            routerParam = new RouterParam();
            IDataBinder<XParam> coder = mCoders.get(XParam.class);
            coder.generateMethod(creator, release, param, item, routerParam);
        }
        Parcelable parcelable = item.getAnnotation(Parcelable.class);
        if (null != parcelable) {
            routerParam = new RouterParam();
            IDataBinder<Parcelable> coder = mCoders.get(Parcelable.class);
            coder.generateMethod(creator, release, parcelable, item, routerParam);
        }
        Serializable serializable = item.getAnnotation(Serializable.class);
        if (serializable != null) {
            routerParam = new RouterParam();
            IDataBinder<Serializable> coder = mCoders.get(Serializable.class);
            coder.generateMethod(creator, release, serializable, item, routerParam);
        }
        return routerParam;
    }

}
