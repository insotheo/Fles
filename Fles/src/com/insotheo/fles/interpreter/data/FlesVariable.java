package com.insotheo.fles.interpreter.data;

import com.insotheo.fles.interpreter.InterpreterData;
import com.insotheo.fles.interpreter.InterpreterExceptions;

public class FlesVariable {
    private final String typeName;
    private FlesValue value;

    public FlesVariable(String type, FlesValue value) throws Exception {
        if (type.equals("auto")) {
            type = InterpreterData.findAutoType(value);
        }
        this.typeName = type;
        setValue(value);
    }

    public FlesVariable(String type) throws Exception {
        if (type.equals("auto")) {
            InterpreterExceptions.throwRuntimeError("Can't find a type instead of auto if the value is null!");
        }
        this.typeName = type;
        setValue(new FlesValue(getTypeName(), ""));
    }

    public String getTypeName() {
        return typeName;
    }

    public FlesValue getValue() {
        return value;
    }

    public void setValue(FlesValue value) {
        this.value = value;
    }

    public void setData(Object data) throws Exception {
        this.value = new FlesValue(getTypeName(), data);
    }

}
