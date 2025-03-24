package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.data.DataType;
import com.insotheo.fles.interpreter.data.ValueType;

public class IntDataType extends DataType {

    public IntDataType() {
        super(ValueType.Numeric);
    }

    public static boolean isDataMatch(Object data) {
        return data instanceof Integer;
    }

    @Override
    public Integer cast(Object data) throws Exception {
        try {
            if (data instanceof String) {
                if (data.toString().isEmpty()) {
                    return null;
                }
                return Integer.parseInt(data.toString());
            } else if (data instanceof Character) {
                return (int) data.toString().charAt(0);
            } else if (data instanceof Number) {
                return ((Number) data).intValue();
            }
            return (int) data;
        } catch (Exception e) {
            InterpreterExceptions.throwCastFailedError("int");
        }
        return null;
    }

}
