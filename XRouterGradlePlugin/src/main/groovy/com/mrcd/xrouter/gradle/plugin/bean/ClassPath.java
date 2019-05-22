package com.mrcd.xrouter.gradle.plugin.bean;

import java.util.List;
import java.util.Objects;

/**
 * 同注解处理器中的ClassPath
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

    public ClassPath() {
    }

    public String getRouterName() {
        return getPath();
    }

    @Override
    public String toString() {
        return "ClassPath{" + "path='" + path + '\'' + ", simpleName='" + simpleName + '\'' + ", className='" + className + '\'' + ", params=" + params + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassPath path1 = (ClassPath) o;
        return path != null && path.equalsIgnoreCase(path1.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
