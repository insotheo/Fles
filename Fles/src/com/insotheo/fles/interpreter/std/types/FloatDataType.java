package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.variable.DataType;

public class FloatDataType extends DataType {

    public FloatDataType() {
        super("float");
    }

    public static boolean isDataMatch(String value) throws Exception {
        try{
            double parsedDouble = Double.parseDouble(value);
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }

    @Override
    public String inferValue(String value) throws Exception {
        try{
            double val = Double.parseDouble(value);
            return String.valueOf(val);
        }
        catch (Exception ex){
            if(value == null || value.trim().isEmpty()){
                return null;
            }
            InterpreterExceptions.throwCastFailedError(this.typeName);
            return null;
        }
    }

    @Override
    public boolean isNumericType() {
        return true;
    }
}
