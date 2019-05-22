package com.mrcd.xrouter.compiler.core.databinder.impl;

import com.mrcd.xrouter.annotation.Parcelable;
import com.mrcd.xrouter.compiler.bean.RouterParam;
import com.mrcd.xrouter.compiler.core.coder.CoderFactory;
import com.mrcd.xrouter.compiler.core.coder.ITypeCoder;
import com.mrcd.xrouter.compiler.core.databinder.IDataBinder;
import com.mrcd.xrouter.compiler.utils.TextUtils;
import com.squareup.javapoet.MethodSpec.Builder;
import javax.lang.model.element.Element;

/**
 * 处理Parcelable注解的数据绑定
 */
public class ParcelableDataBinder implements IDataBinder<Parcelable> {

    @Override
    public void generateMethod(Builder creator, Builder release, Parcelable parcelable, Element item, RouterParam routerParam) {
        String fieldName = item.getSimpleName().toString();
        String paramName = TextUtils.isEmpty(parcelable.name()) ? fieldName : parcelable.name();
        ITypeCoder coder = CoderFactory.getInstance().getParcelableCoder();
        if (null != coder) {
            creator.addStatement(coder.generateCode(fieldName, paramName), item.asType());
            String releaseCode = coder.releaseCode(fieldName, paramName);
            if (!TextUtils.isEmpty(releaseCode)) {
                creator.addStatement(releaseCode);
            }
            routerParam.setParamName(paramName).setFieldName(fieldName).setType("android.os.Parcelable");
        }
    }
}
