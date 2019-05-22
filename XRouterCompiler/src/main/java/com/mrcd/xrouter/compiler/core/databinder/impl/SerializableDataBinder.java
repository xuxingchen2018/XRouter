package com.mrcd.xrouter.compiler.core.databinder.impl;

import com.mrcd.xrouter.annotation.Serializable;
import com.mrcd.xrouter.compiler.bean.RouterParam;
import com.mrcd.xrouter.compiler.core.coder.CoderFactory;
import com.mrcd.xrouter.compiler.core.coder.ITypeCoder;
import com.mrcd.xrouter.compiler.core.databinder.IDataBinder;
import com.mrcd.xrouter.compiler.utils.TextUtils;
import com.squareup.javapoet.MethodSpec.Builder;
import javax.lang.model.element.Element;

/**
 * 处理Serializable注解的数据绑定
 */
public class SerializableDataBinder implements IDataBinder<Serializable> {

    @Override
    public void generateMethod(Builder creator, Builder release, Serializable serializable, Element item, RouterParam routerParam) {
        String fieldName = item.getSimpleName().toString();
        String paramName = TextUtils.isEmpty(serializable.name()) ? fieldName : serializable.name();
        ITypeCoder coder = CoderFactory.getInstance().getSerializableCoder();
        if (null != coder) {
            creator.addStatement(coder.generateCode(fieldName, paramName), item.asType());
            String releaseCode = coder.releaseCode(fieldName, paramName);
            if (!TextUtils.isEmpty(releaseCode)) {
                creator.addStatement(releaseCode);
            }
            routerParam.setParamName(paramName).setFieldName(fieldName).setType(java.io.Serializable.class.getName());
        }
    }
}
