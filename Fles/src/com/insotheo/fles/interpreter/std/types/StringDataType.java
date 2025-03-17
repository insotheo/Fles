package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.variable.DataType;

public class StringDataType extends DataType {

    public StringDataType() {
        super("string");
    }

    @Override
    public boolean isString() {
        return true;
    }

    public static boolean isDataMatch(String data) throws Exception{
        return true;
    }

    @Override
    public String inferValue(String value) throws Exception {
        return value;
    }
}
