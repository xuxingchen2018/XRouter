package com.mrcd.xrouter.builder;

import android.content.Intent;

/**
 * intent 参数设置器接口 现已经实现了8中基本数据类型以及string的参数构建器，如果还需要扩展，可以实现该接口
 *
 * @param <C> 类型参数
 */
public interface IValueBuilder<C> {

    void setIntent(Intent intent, String key, C value);

    C getValue(Intent intent, String key);

    C getValue(Intent intent, String key, C defValue);

}
