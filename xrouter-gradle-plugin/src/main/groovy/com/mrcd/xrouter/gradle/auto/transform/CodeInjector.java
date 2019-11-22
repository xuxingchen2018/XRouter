package com.mrcd.xrouter.gradle.auto.transform;

import java.io.File;

/**
 * 代码注入
 * <p>
 * {@link com.mrcd.xrouter.gradle.auto.transform.injector.DirectoryInjector}
 * {@link com.mrcd.xrouter.gradle.auto.transform.injector.JarInjector}
 */
public interface CodeInjector {

    /**
     * 注入代码
     *
     * @param target class或者jar文件
     */
    void doInject(File target);

}
