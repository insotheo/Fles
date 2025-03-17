package com.insotheo.fles.interpreter.variable;

import com.insotheo.fles.interpreter.InterpreterData;
import com.insotheo.fles.interpreter.InterpreterExceptions;

public class FlesValue {
    private String data;
    private final ValueType type;

    public FlesValue(String val, String dataType) throws Exception{
        data = val;
        this.type = FlesValue.identifyType(dataType);
    }

    public FlesValue(String val, ValueType type){
        data = val;
        this.type = type;
    }

    public static ValueType identifyType(String dataType) throws Exception{
        if(InterpreterData.getDataType(dataType).isNumericType()){
            return ValueType.Numeric;
        }
        else if(InterpreterData.getDataType(dataType).isChar()){
            return ValueType.CharLiteral;
        }
        else if(InterpreterData.getDataType(dataType).isString()){
            return ValueType.StringLiteral;
        }

        return ValueType.Unknown;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ValueType getType() {
        return type;
    }

    //Some operations
    public void add(FlesValue v) throws Exception{
        if(type == ValueType.Numeric && v.type == ValueType.Numeric){
            double a = Double.parseDouble(data);
            double b = Double.parseDouble(v.data);
            data = String.valueOf(a + b);
            return;
        }
        else if(type == ValueType.Numeric && v.type == ValueType.CharLiteral){
            double a = Double.parseDouble(data);
            char b = v.getData().charAt(0);
            data = String.valueOf(a + (int)b);
            return;
        }
        else if(type == ValueType.CharLiteral && v.type == ValueType.Numeric){
            char a = data.charAt(0);
            double b = Double.parseDouble(v.getData());
            data = String.valueOf((char)((int)a + (int)b)); //Maybe its gonna be an int...
            return;
        }

        else if(type == ValueType.StringLiteral && v.type == ValueType.Numeric){
            data += v.getData();
            return;
        }
        else if(type == ValueType.StringLiteral && v.type == ValueType.CharLiteral){
            data += String.valueOf(v.getData().charAt(0));
            return;
        }
        else if(type == ValueType.CharLiteral && v.type == ValueType.StringLiteral){
            data = data.charAt(0) + v.getData();
            return;
        }
        else if(type == ValueType.StringLiteral && v.type == ValueType.StringLiteral){
            data += v.getData();
            return;
        }

        InterpreterExceptions.throwRuntimeError(String.format("Can't do an add operation with %s and %s", type, v.type));
    }

    public void subtract(FlesValue v) throws Exception{
        if(type == ValueType.Numeric && v.type == ValueType.Numeric){
            double a = Double.parseDouble(data);
            double b = Double.parseDouble(v.data);
            data = String.valueOf(a - b);
            return;
        }
        else if(type == ValueType.Numeric && v.type == ValueType.CharLiteral){
            double a = Double.parseDouble(data);
            char b = v.getData().charAt(0);
            data = String.valueOf(a - (int)b);
            return;
        }
        else if(type == ValueType.CharLiteral && v.type == ValueType.Numeric){
            char a = data.charAt(0);
            double b = Double.parseDouble(v.getData());
            data = String.valueOf((int)a - (int)b);
            return;
        }

        InterpreterExceptions.throwRuntimeError(String.format("Can't do a subtract operation with %s and %s", type, v.type));
    }

    public void multiply(FlesValue v) throws Exception{
        if(type == ValueType.Numeric && v.type == ValueType.Numeric){
            double a = Double.parseDouble(data);
            double b = Double.parseDouble(v.data);
            data = String.valueOf(a * b);
            return;
        }

        InterpreterExceptions.throwRuntimeError(String.format("Can't do a multiply operation with %s and %s", type, v.type));
    }

    public void divide(FlesValue v) throws Exception {
        if(type == ValueType.Numeric && v.type == ValueType.Numeric){
            double a = Double.parseDouble(data);
            double b = Double.parseDouble(v.data);
            if(b == 0){
                InterpreterExceptions.throwRuntimeError("Zero division exception!");
                return;
            }
            data = String.valueOf(a / b);
            return;
        }

        InterpreterExceptions.throwRuntimeError(String.format("Can't do a divide operation with %s and %s", type, v.type));
    }

}
