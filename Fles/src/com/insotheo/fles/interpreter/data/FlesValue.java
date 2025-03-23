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
                double result = (double) getData() + (double) v.getData();
                setData(result);
            } else if (getType() == ValueType.Numeric && v.getType() == ValueType.CharLiteral) {
                double result = (double) getData() + (int)v.getData().toString().charAt(0);
                setData(result);
            } else if (getType() == ValueType.CharLiteral && v.getType() == ValueType.Numeric) {
                char result = (char) ((int) getData().toString().charAt(0) + (int) (double) v.getData());
                setData(result);
            } else if (getType() == ValueType.StringLiteral && v.getType() == ValueType.Numeric) {
                String result = getData().toString() + v.getData().toString();
                setData(result);
            } else if (getType() == ValueType.StringLiteral && v.getType() == ValueType.CharLiteral) {
                String result = getData().toString() + (char)v.getData();
                setData(result);
            } else if (getType() == ValueType.CharLiteral && v.getType() == ValueType.StringLiteral) {
                String result = getData().toString() + v.getData().toString();
                setData(result);
            } else if (getType() == ValueType.StringLiteral && v.getType() == ValueType.StringLiteral) {
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
}