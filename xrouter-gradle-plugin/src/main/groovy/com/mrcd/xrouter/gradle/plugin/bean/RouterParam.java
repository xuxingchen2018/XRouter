package com.mrcd.xrouter.gradle.plugin.bean;

/**
 * 对注解的参数封装的实体类
 */
public class RouterParam {

    /**
     * 参数名
     */
    private String mParamName;

    /**
     * 属性名
     */
    private String mFieldName;

    /**
     * 参数类型
     */
    private String mType;

    private boolean isHuge = false;

    public String getParamName() {
        return mParamName;
    }

    public RouterParam setParamName(String paramName) {
        mParamName = paramName;
        return this;
    }

    public String getFieldName() {
        return mFieldName;
    }

    public RouterParam setFieldName(String fieldName) {
        mFieldName = fieldName;
        return this;
    }

    public String getType() {
        return mType;
    }

    public RouterParam setType(String type) {
        mType = type;
        return this;
    }

    public boolean isHuge() {
        return isHuge;
    }

    public RouterParam setHuge(boolean huge) {
        isHuge = huge;
        return this;
    }

    @Override
    public String toString() {
        return "RouterParam{" + "mParamName='" + mParamName + '\'' + ", mFieldName='" + mFieldName + '\'' + ", mType='" + mType + '\'' + ", isHuge=" + isHuge + '}';
    }
}
