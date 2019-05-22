package com.mrcd.xrouter.compiler.core.databinder;

import com.mrcd.xrouter.compiler.bean.RouterParam;
import com.squareup.javapoet.MethodSpec;
import javax.lang.model.element.Element;

/**
 * 数据绑定
 * 主要生成注入数据和释放数据的方法代码
 *
 * @param <T> 数据类型
 */
public interface IDataBinder<T> {

    /**
     * 生成注入数据和释放数据方法
     *
     * @param creator     绑定数据的方法
     * @param release     释放数据的方法
     * @param t           方法类型
     * @param element     被扫描到的元素
     * @param routerParam 参数
     */
    void generateMethod(MethodSpec.Builder creator, MethodSpec.Builder release, T t, Element element, RouterParam routerParam);

}
