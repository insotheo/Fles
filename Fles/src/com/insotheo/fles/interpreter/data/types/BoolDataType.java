package com.insotheo.fles.interpreter.data.types;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.data.DataType;
import com.insotheo.fles.interpreter.data.ValueType;

public class BoolDataType extends DataType {
    public BoolDataType() {
        super(ValueType.Boolean);
    }

    public static boolean isDataMatch(Object data) {
        return data instanceof Boolean;
    }

    @Override
    public Boolean cast(Object data) throws Exception {
        try {
            if (data instanceof Number) {
                return ((Number) data).intValue() != 0;
            } else if (data instanceof String && data.toString().isEmpty()) {
                return false;
            }
            return (boolean) data;
        } catch (Exception e) {
            InterpreterExceptions.throwCastFailedError("bool");
        }
        return null;
    }
}
