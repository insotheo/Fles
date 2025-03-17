package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.variable.DataType;

public class VoidDataType extends DataType {

    public VoidDataType() {
        super("void");
    }

    public static boolean isDataMatch(String value) throws Exception {
        return true;
    }

    @Override
    public String inferValue(String value) throws Exception {
        return "";
    }
}
