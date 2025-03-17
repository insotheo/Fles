package com.insotheo.fles.interpreter.variable;

import com.insotheo.fles.interpreter.InterpreterData;
import com.insotheo.fles.interpreter.InterpreterExceptions;

public class FlesVariable {
    private final DataType type;
    private final String name;
    private FlesValue value;

    public FlesVariable(String type, String name, String value) throws Exception{
        if(type.equals("auto")){
            type = InterpreterData.findAutoType(value);
        }
        this.type = InterpreterData.getDataType(type);
        this.name = name;
        setValue(value);
    }

    public FlesVariable(String type, String name) throws Exception{
        if(type.equals("auto")){
            InterpreterExceptions.throwRuntimeError("Can't find a type instead of auto if the value is null!");
        }
        this.type = InterpreterData.getDataType(type);
        this.name = name;
        setValue(new FlesValue("", this.type.getName()));
    }

    public DataType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public FlesValue getValue() {
        return value;
    }

    public void setValue(FlesValue value) {
        this.value = value;
    }

    public void setValue(String value) throws Exception{
        this.value = new FlesValue(type.inferValue(value), type.getName());
    }

    public void setData(String data) throws Exception{
        String resultData = type.inferValue(data);
        this.value.setData(resultData);
    }

}
