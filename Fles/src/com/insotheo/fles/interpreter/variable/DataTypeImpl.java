package com.insotheo.fles.interpreter.variable;

import com.insotheo.fles.interpreter.InterpreterExceptions;

public class DataTypeImpl {
    public static DataType parseDataType(String type) throws Exception{
        switch (type){
            case "int": return DataType.Int;
            case "float": return DataType.Float;
            case "string": return DataType.StringType;
            case "char": return DataType.Char;
            case "void": return DataType.Void;
            case "auto": return DataType.Auto;
        }

        InterpreterExceptions.throwUnknownDataType(type);
        return null;
    }
}
