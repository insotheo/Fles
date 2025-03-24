package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.data.DataType;
import com.insotheo.fles.interpreter.data.ValueType;

public class StringDataType extends DataType {

    public StringDataType() {
        super(ValueType.StringLiteral);
    }

    public static boolean isDataMatch(Object data) {
        return data instanceof String;
    }

    @Override
    public String cast(Object data) throws Exception {
        return data.toString();
    }
}
