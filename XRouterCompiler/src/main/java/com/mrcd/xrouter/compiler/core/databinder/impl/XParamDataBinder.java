package com.mrcd.xrouter.compiler.core.databinder.impl;

import com.mrcd.xrouter.annotation.XParam;
import com.mrcd.xrouter.compiler.bean.RouterParam;
import com.mrcd.xrouter.compiler.core.coder.CoderFactory;
import com.mrcd.xrouter.compiler.core.coder.ITypeCoder;
import com.mrcd.xrouter.compiler.core.databinder.IDataBinder;
import com.mrcd.xrouter.compiler.exception.XRouterException;
import com.mrcd.xrouter.compiler.utils.TextUtils;
import com.squareup.javapoet.MethodSpec.Builder;
import javax.lang.model.element.Element;

/**
 * 处理XParam注解的数据绑定
 */
public class XParamDataBinder implements IDataBinder<XParam> {

    private static final String UNKNOWN_DATA_TYPE = "All of the data type must be short, int, long, float, double, byte, char, boolean, string, Serializable types of data must use Serializable annotation, Parcelable types of data must use Parcelable annotation, huge amounts of data can use XParam isLarge field";

    @Override
    public void generateMethod(Builder creator, Builder release, XParam param, Element item, RouterParam routerParam) {
        boolean isLarge = param.isLarge();
        String fieldName = item.getSimpleName().toString();
        String paramName = TextUtils.isEmpty(param.name()) ? fieldName : param.name();
        String type = item.asType().toString();
        //设置参数的相关属性
        routerParam.setType(type).setFieldName(fieldName).setParamName(paramName);

        ITypeCoder coder;
        if (isLarge) {
            coder = CoderFactory.getInstance().getLargeCoder();
            //如果是巨量的数据的话，内部会用静态变量的形式存储，所以此处标记一下
            routerParam.setHuge(true);
        } else {
            coder = CoderFactory.getInstance().getCoder(type);
        }
        if (null != coder) {
            creator.addStatement(coder.generateCode(fieldName, paramName));
            String releaseCode = coder.releaseCode(fieldName, paramName);
            if (!TextUtils.isEmpty(releaseCode)) {
                release.addStatement(releaseCode);
            }
        } else {
            throw new XRouterException(UNKNOWN_DATA_TYPE);
        }
    }
}
