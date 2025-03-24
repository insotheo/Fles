package com.insotheo.fles.interpreter.data;

import com.insotheo.fles.interpreter.InterpreterData;
import com.insotheo.fles.interpreter.InterpreterExceptions;

public class FlesValue {
    private Object data;
    private final ValueType type;

    public FlesValue(String typeName, Object data) throws Exception {
        this.type = InterpreterData.getDataType(typeName).getDataValueType();
        this.data = InterpreterData.getDataType(typeName).cast(data);
    }

    public FlesValue(ValueType type, Object data) {
        this.data = data;
        this.type = type;
    }

    public <T> T getData() {
        return (T) data;
    }

    public <T> void setData(T data) {
        this.data = data;
    }

    public ValueType getType() {
        return type;
    }

    //Some operations
    public void add(FlesValue v) throws Exception {
        try {
            if (getType() == ValueType.Numeric && v.getType() == ValueType.Numeric) {
                double result = ((Number) getData()).doubleValue() + ((Number) v.getData()).doubleValue();
                setData(result);
            } else if (getType() == ValueType.Numeric && v.getType() == ValueType.CharLiteral) {
                double result = (double) getData() + (int)v.getData().toString().charAt(0);
                setData(result);
            } else if (getType() == ValueType.CharLiteral && v.getType() == ValueType.Numeric) {
                char result = (char) ((int) getData().toString().charAt(0) + (int) (double) v.getData());
                setData(result);
            } else if(getType() == ValueType.StringLiteral && v.getType() != null){
                String result = getData().toString() + v.getData().toString();
                setData(result);
            }
        } catch (Exception e) {
            InterpreterExceptions.throwRuntimeError(String.format("Can't do an addition with '%s' and '%s'", getType(), v.getType()));
        }
    }


    public void subtract(FlesValue v) throws Exception {
        try {
            if (getType() == ValueType.Numeric && v.getType() == ValueType.Numeric) {
                double result = ((Number) getData()).doubleValue() - ((Number) v.getData()).doubleValue();
                setData(result);
            } else if (getType() == ValueType.Numeric && v.getType() == ValueType.CharLiteral) {
                double result = (double) getData() - (int)v.getData().toString().charAt(0);
                setData(result);
            } else if (getType() == ValueType.CharLiteral && v.getType() == ValueType.Numeric) {
                char result = (char) ((int) getData().toString().charAt(0) - (int) (double) v.getData());
                setData(result);
            }
        } catch (Exception e) {
            InterpreterExceptions.throwRuntimeError(String.format("Can't do a subtraction with '%s' and '%s'", getType(), v.getType()));
        }
    }

    public void multiply(FlesValue v) throws Exception {
        try {
            if (getType() == ValueType.Numeric && v.getType() == ValueType.Numeric) {
                double result = ((Number) getData()).doubleValue() * ((Number) v.getData()).doubleValue();
                setData(result);
            }
        } catch (Exception e) {
            InterpreterExceptions.throwRuntimeError(String.format("Can't do a multiplication with '%s' and '%s'", getType(), v.getType()));
        }
    }

    public void divide(FlesValue v) throws Exception {
        if (getType() == ValueType.Numeric && v.getType() == ValueType.Numeric) {
            double a = ((Number) getData()).doubleValue();
            double b = ((Number) v.getData()).doubleValue();
            if (b == 0) {
                InterpreterExceptions.throwRuntimeError("Zero division exception!");
                return;
            }
            double result = a / b;
            setData(result);
            return;
        }
        InterpreterExceptions.throwRuntimeError(String.format("Can't do a division with '%s' and '%s'", getType(), v.getType()));
    }

    public FlesValue equal(FlesValue v) throws Exception {
        if(getType() == ValueType.Numeric && v.getType() == ValueType.Numeric){
            return new FlesValue(ValueType.Boolean, ((Number) getData()).doubleValue() == ((Number) v.getData()).doubleValue());
        }
        else if(getType() == ValueType.Numeric && v.getType() == ValueType.CharLiteral){
            return new FlesValue(ValueType.Boolean, ((Number) getData()).doubleValue() == (double)(int)(char)v.getData());
        }
        else if(getType() == ValueType.CharLiteral && v.getType() == ValueType.Numeric){
            return new FlesValue(ValueType.Boolean, (double)(int)(char)getData() == ((Number) v.getData()).doubleValue());
        }
        else if(getType() == ValueType.StringLiteral && v.getType() == ValueType.StringLiteral){
            return new FlesValue(ValueType.Boolean, getData().toString().equals(v.getData().toString()));
        }
        else if(getType() == ValueType.CharLiteral && v.getType() == ValueType.CharLiteral){
            return new FlesValue(ValueType.Boolean, (char)getData() == (char)(v.getData()));
        }
        else if(getType() == ValueType.Boolean && v.getType() == ValueType.Boolean){
            return new FlesValue(ValueType.Boolean, (boolean)getData() == (boolean)(v.getData()));
        }
        else{
            try {
                return new FlesValue(ValueType.Boolean, getData().equals(v.getData()));
            }catch(Exception e){
                InterpreterExceptions.throwRuntimeError(String.format("Can't do comparing with '%s' and '%s'", getType(), v.getType()));
                return null;
            }
        }
    }

    public FlesValue notEqual(FlesValue v) throws Exception{
        FlesValue equalResult = equal(v);
        if(!(equalResult.getData() instanceof Boolean)){
            InterpreterExceptions.throwRuntimeError("Unknown error: bool is not bool...");
        }
        boolean value = (boolean)equalResult.getData();
        return new FlesValue(ValueType.Boolean, !value);
    }

    public FlesValue greaterLessThan(FlesValue v, boolean greater) throws Exception{ //greater is true: >; greater is false: <
        if(getType() == ValueType.Numeric && v.getType() == ValueType.Numeric){
            if(greater) {
                return new FlesValue(ValueType.Boolean, ((Number) getData()).doubleValue() > ((Number) v.getData()).doubleValue());
            }
            return new FlesValue(ValueType.Boolean, ((Number) getData()).doubleValue() < ((Number) v.getData()).doubleValue());
        }
        else if(getType() == ValueType.Numeric && v.getType() == ValueType.CharLiteral){
            if(greater) {
                return new FlesValue(ValueType.Boolean, ((Number) getData()).doubleValue() > (double) (int) (char) (v.getData()));
            }
            return new FlesValue(ValueType.Boolean, ((Number) getData()).doubleValue() < (double) (int) (char) (v.getData()));
        }
        else if(getType() == ValueType.CharLiteral && v.getType() == ValueType.Numeric){
            if(greater) {
                return new FlesValue(ValueType.Boolean, (double) (int) (char) (getData()) > ((Number) getData()).doubleValue());
            }
            return new FlesValue(ValueType.Boolean, (double) (int) (char) (getData()) < ((Number) getData()).doubleValue());
        }
        else if(getType() == ValueType.CharLiteral && v.getType() == ValueType.CharLiteral){
            if(greater) {
                return new FlesValue(ValueType.Boolean, (int) (char) (getData()) > (int) (char) (v.getData()));
            }
            return new FlesValue(ValueType.Boolean, (int) (char) (getData()) < (int) (char) (v.getData()));
        }
        InterpreterExceptions.throwRuntimeError(String.format("Can't do comparing with '%s' and '%s'", getType(), v.getType()));
        return null;
    }

    public FlesValue greaterLessOrEqual(FlesValue v, boolean greater) throws Exception{
        FlesValue greaterResult = greaterLessThan(v, greater);
        FlesValue equalResult = equal(v);
        if(!(greaterResult.getData() instanceof Boolean || equalResult.getData() instanceof Boolean)){
            InterpreterExceptions.throwRuntimeError("Unknown error: bool is not bool...");
        }
        return new FlesValue(ValueType.Boolean,(boolean)greaterResult.getData() || (boolean)equalResult.getData());
    }

    public FlesValue and(FlesValue v) throws Exception{
        if(!(getData() instanceof Boolean || v.getData() instanceof Boolean)){
            InterpreterExceptions.throwRuntimeError("Unknown error: bool is not bool...");
        }
        boolean self = (boolean)getData();
        boolean vBool = (boolean)v.getData();
        return new FlesValue(ValueType.Boolean, self && vBool);
    }

    public FlesValue or(FlesValue v) throws Exception{
        if(!(getData() instanceof Boolean || v.getData() instanceof Boolean)){
            InterpreterExceptions.throwRuntimeError("Unknown error: bool is not bool...");
        }
        boolean self = (boolean)getData();
        boolean vBool = (boolean)v.getData();
        return new FlesValue(ValueType.Boolean, self || vBool);
    }

    public FlesValue not() throws Exception{
        if(!(getData() instanceof Boolean)){
            InterpreterExceptions.throwRuntimeError("Unknown error: bool is not bool...");
        }
        return new FlesValue(ValueType.Boolean, !((boolean) getData()));
    }

}