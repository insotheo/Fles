package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.variable.DataType;

public class IntDataType extends DataType {

    public IntDataType() {
        super("int");
    }

    public static boolean isDataMatch(String value) throws Exception {
        try{
            int parsedInt = Integer.parseInt(value);
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }

    @Override
    public String inferValue(String value) throws Exception {
        try{
            double parsedVal = Double.parseDouble(value);
            int val = (int)parsedVal;
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
