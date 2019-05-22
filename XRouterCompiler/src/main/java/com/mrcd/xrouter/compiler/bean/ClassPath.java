package com.mrcd.xrouter.compiler.bean;

import java.util.List;
import java.util.Objects;

/**
 * 对注解的class封装的实体类
 */
public class ClassPath {

    /**
     * 在注解上声明的路径
     */
    private String path;

    /**
     * 类名称，不包含全路径
     */
    private String simpleName;

    /**
     * 类名，包含全路径
     */
    private String className;

    /**
     * 路由参数
     */
    private List<RouterParam> params;

    public String getPath() {
        return path;
    }

    public ClassPath setPath(String path) {
        this.path = path;
        return this;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public ClassPath setSimpleName(String simpleName) {
        this.simpleName = simpleName;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public ClassPath setClassName(String className) {
        this.className = className;
        return this;
    }

    public List<RouterParam> getParams() {
        return params;
    }

    public ClassPath setParams(List<RouterParam> params) {
        this.params = params;
        return this;
    }

    public ClassPath(String path, String simpleName, String className) {
        this.path = path;
        this.simpleName = simpleName;
        this.className = className;
    }

    public ClassPath() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassPath classPath = (ClassPath) o;
        return path.equals(classPath.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
