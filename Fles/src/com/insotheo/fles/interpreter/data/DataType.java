package com.insotheo.fles.interpreter.data;

public class DataType {
    protected final ValueType dataValueType;

    protected DataType(ValueType dataValueType) {
        this.dataValueType = dataValueType;
    }

    public static boolean isDataMatch(Object data) {
        return false;
    }

    public ValueType getDataValueType() {
        return dataValueType;
    }

    public Object cast(Object data) throws Exception {
        return null;
    }

}
