package com.insotheo.fles.interpreter.std.types;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.data.DataType;
import com.insotheo.fles.interpreter.data.ValueType;

public class FloatDataType extends DataType {

    public FloatDataType() {
        super(ValueType.Numeric);
    }

    public static boolean isDataMatch(Object data) {
        return data instanceof Float;
    }

    @Override
    public Double cast(Object data) throws Exception {
        try{
            if(data instanceof String){
                if(data.toString().isEmpty()){
                    return null;
                }
                return Double.parseDouble(data.toString());
            }
            else if(data instanceof Character){
                return (double)(int)data.toString().charAt(0);
            }
            else if(data instanceof Number){
                return ((Number)data).doubleValue();
            }
            return (double)data;
        } catch (Exception e) {
            InterpreterExceptions.throwCastFailedError("float");
        }
        return null;
    }
}
