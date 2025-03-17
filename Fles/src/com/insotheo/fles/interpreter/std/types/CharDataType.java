package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.variable.DataType;

public class CharDataType extends DataType {

    public CharDataType() {
        super("char");
    }

    public static boolean isDataMatch(String value) throws Exception {
        return value.length() == 1 || IntDataType.isDataMatch(value);
    }

    @Override
    public boolean isChar(){
        return true;
    }

    @Override
    public String inferValue(String value) throws Exception {
        if(IntDataType.isDataMatch(value)){
            int code = Integer.parseInt(value);
            return String.valueOf((char)code);
        }

        else if(FloatDataType.isDataMatch(value)){
            try{
                int code = (int)Double.parseDouble(value);
                return String.valueOf((char)code);
            }
            catch (Exception ex){
                if(value == null || value.trim().isEmpty()){
                    return null;
                }
                InterpreterExceptions.throwCastFailedError(this.typeName);
                return null;
            }
        }

        return String.valueOf(value.charAt(0));
    }
}
