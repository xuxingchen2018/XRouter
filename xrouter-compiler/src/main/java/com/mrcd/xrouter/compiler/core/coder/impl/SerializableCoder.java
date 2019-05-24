package com.mrcd.xrouter.compiler.core.coder.impl;

import com.mrcd.xrouter.compiler.core.coder.ITypeCoder;

public class SerializableCoder implements ITypeCoder {

    @Override
    public String generateCode(String fieldName, String key) {
        return "target." + fieldName + " = ($T)wrapper.getSerializable(intent, \"" + key + "\")";
    }

    @Override
    public String releaseCode(String fieldName, String key) {
        return "";
    }
}
