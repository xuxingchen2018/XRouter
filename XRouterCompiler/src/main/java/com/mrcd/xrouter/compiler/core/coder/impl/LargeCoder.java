package com.mrcd.xrouter.compiler.core.coder.impl;

import com.mrcd.xrouter.compiler.core.coder.ITypeCoder;

public class LargeCoder implements ITypeCoder {

    @Override
    public String generateCode(String fieldName, String key) {
        return "target." + fieldName + " = wrapper.getLargeValue(\"" + key + "\")";
    }

    @Override
    public String releaseCode(String fieldName, String key) {
        return "wrapper.clearLargeValue(\"" + key + "\")";
    }
}
