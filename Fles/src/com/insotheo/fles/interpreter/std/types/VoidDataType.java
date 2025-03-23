package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.data.DataType;
import com.insotheo.fles.interpreter.data.ValueType;

public class VoidDataType extends DataType {

    public VoidDataType() {
        super(ValueType.Unknown);
    }

    public static boolean isDataMatch(Object data) {
        return true;
    }

    @Override
    public Object cast(Object data) throws Exception {
        return null;
    }
}
