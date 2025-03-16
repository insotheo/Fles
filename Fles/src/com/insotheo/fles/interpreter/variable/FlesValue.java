package com.insotheo.fles.interpreter.variable;

import com.insotheo.fles.interpreter.InterpreterExceptions;

public class FlesValue {
    private String data;
    private final ValueType type;

    public FlesValue(String val, ValueType type){
        data = val;
        this.type = type;
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

    public static ValueType parseTypeFromDataType(DataType t, String data){
        if(t == DataType.Int){
            return ValueType.NumberValue;
        }
        else if(t == DataType.StringType){
            return ValueType.StringValue;
        }
        else if(t == DataType.Char){
            return ValueType.CharValue;
        }

        else if(t == DataType.Auto){
            return inferValueType(data);
        }

        return ValueType.NoneValue;
    }

    public static ValueType inferValueType(String value) {
        if (value == null || value.isEmpty()) {
            return ValueType.NoneValue;
        }

        try {
            Double.parseDouble(value);
            return ValueType.NumberValue;
        }
        catch (NumberFormatException e) {
        }

        if (value.length() == 1) {
            return ValueType.CharValue;
        }

        return ValueType.StringValue;
    }

    //Some operations
    public void add(FlesValue valueToAdd) throws Exception{
        if(type == ValueType.NumberValue){
            if(valueToAdd.type == ValueType.NumberValue) {
                double numA = Double.parseDouble(data);
                double numB = Double.parseDouble(valueToAdd.data);
                numA += numB;
                data = String.valueOf(numA);
            }

            else if(valueToAdd.type == ValueType.CharValue){
                double numA = Double.parseDouble(data);
                char charB = valueToAdd.data.charAt(0);
                numA += (double)((int)charB);
                data = String.valueOf(numA);
            }

            else if(valueToAdd.type == ValueType.StringValue){
                InterpreterExceptions.throwRuntimeError("Can't add a string to a number!");
            }
        }

        else if(type == ValueType.CharValue){
            if(valueToAdd.type == ValueType.NumberValue) {
                char charA = data.charAt(0);
                double numB = Double.parseDouble(valueToAdd.data);
                charA += (char)((int)numB);
                data = String.valueOf(charA);
            }

            else if(valueToAdd.type == ValueType.CharValue){
                char charA = data.charAt(0);
                char charB = valueToAdd.data.charAt(0);
                charA += charB;
                data = String.valueOf(charA);
            }

            else if(valueToAdd.type == ValueType.StringValue){
                InterpreterExceptions.throwRuntimeError("Can't add a string to a character!");
            }
        }

        else if(type == ValueType.StringValue){
            if(valueToAdd.type == ValueType.NumberValue) {
                double numB = Double.parseDouble(valueToAdd.data);
                data += String.valueOf(numB);
            }

            else if(valueToAdd.type == ValueType.CharValue){
                char charB = valueToAdd.data.charAt(0);
                data += String.valueOf(charB);
            }

            else if(valueToAdd.type == ValueType.StringValue){
                data += valueToAdd.data;
            }
        }
    }

    public void subtract(FlesValue valueToSubtract) throws Exception{
        if(type == ValueType.NumberValue){
            if(valueToSubtract.type == ValueType.NumberValue) {
                double numA = Double.parseDouble(data);
                double numB = Double.parseDouble(valueToSubtract.data);
                numA -= numB;
                data = String.valueOf(numA);
            }

            else if(valueToSubtract.type == ValueType.CharValue){
                double numA = Double.parseDouble(data);
                char charB = valueToSubtract.data.charAt(0);
                numA -= (double)((int)charB);
                data = String.valueOf(numA);
            }

            else if(valueToSubtract.type == ValueType.StringValue){
                InterpreterExceptions.throwRuntimeError("Can't subtract a string from number!");
            }
        }

        else if(type == ValueType.CharValue){
            if(valueToSubtract.type == ValueType.NumberValue) {
                char charA = data.charAt(0);
                double numB = Double.parseDouble(valueToSubtract.data);
                charA -= (char)((int)numB);
                data = String.valueOf(charA);
            }

            else if(valueToSubtract.type == ValueType.CharValue){
                char charA = data.charAt(0);
                char charB = valueToSubtract.data.charAt(0);
                charA -= charB;
                data = String.valueOf(charA);
            }

            else if(valueToSubtract.type == ValueType.StringValue){
                InterpreterExceptions.throwRuntimeError("Can't subtract a string from character!");
            }
        }

        else if(type == ValueType.StringValue){
            InterpreterExceptions.throwRuntimeError("Can't subtract from a string!");
        }
    }

    public void multiply(FlesValue valueToMultiply) throws Exception{
        if (type == ValueType.NumberValue) {
            if (valueToMultiply.type == ValueType.NumberValue) {
                double numA = Double.parseDouble(data);
                double numB = Double.parseDouble(valueToMultiply.data);
                numA *= numB;
                data = String.valueOf(numA);
            }
            else {
                InterpreterExceptions.throwRuntimeError("Can't multiply a non-number with a number!");
            }
        }

        else if (type == ValueType.CharValue) {
            if (valueToMultiply.type == ValueType.NumberValue) {
                char charA = data.charAt(0);
                double numB = Double.parseDouble(valueToMultiply.data);
                charA *= (char) ((int) numB);
                data = String.valueOf(charA);
            }
            else {
                InterpreterExceptions.throwRuntimeError("Can't multiply a non-number with a character!");
            }
        }

        else {
            InterpreterExceptions.throwRuntimeError("Can't multiply a string!");
        }
    }

    public void divide(FlesValue valueToDivide) throws Exception {
        if (type == ValueType.NumberValue) {
            if (valueToDivide.type == ValueType.NumberValue) {
                double numA = Double.parseDouble(data);
                double numB = Double.parseDouble(valueToDivide.data);
                if (numB == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                numA /= numB;
                data = String.valueOf(numA);
            }
            else {
                InterpreterExceptions.throwRuntimeError("Can't divide a non-number by a number!");
            }
        }

        else if (type == ValueType.CharValue) {
            if (valueToDivide.type == ValueType.NumberValue) {
                char charA = data.charAt(0);
                double numB = Double.parseDouble(valueToDivide.data);
                if (numB == 0) {
                    InterpreterExceptions.throwRuntimeError("Division by zero!");
                    return;
                }
                charA /= (char) ((int) numB);
                data = String.valueOf(charA);
            }
            else {
                InterpreterExceptions.throwRuntimeError("Can't divide a non-number by a character!");
            }
        }

        else {
            InterpreterExceptions.throwRuntimeError("Can't divide a string!");
        }
    }

}
