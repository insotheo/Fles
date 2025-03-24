package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.data.DataType;
import com.insotheo.fles.interpreter.data.ValueType;

public class CharDataType extends DataType {

    public CharDataType() {
        super(ValueType.CharLiteral);
    }

    public static boolean isDataMatch(Object data) {
        return data instanceof Character;
    }

    @Override
    public Character cast(Object data) throws Exception {
        try {
            if (data instanceof Number) {
                int val = ((Number) data).intValue();
                return (char) val;
            } else if (data instanceof String) {
                if (data.toString().isEmpty()) {
                    return null;
                }
                return data.toString().charAt(0);
            } else {
                return (char) data;
            }
        } catch (Exception ex) {
            InterpreterExceptions.throwCastFailedError("char");
        }
        return null;
    }
}
