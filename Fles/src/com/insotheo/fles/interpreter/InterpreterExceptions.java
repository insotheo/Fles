package com.insotheo.fles.interpreter;

public class InterpreterExceptions{
    public static void throwMainFunctionNotFound() throws Exception{
        throw new Exception("main() function was not found!");
    }

    public static void throwFunctionNotFound(String funcName) throws Exception{
        throw new Exception(String.format("Function '%s' not found!", funcName));
    }

    public static void throwVariableNotFound(String varName) throws Exception{
        throw new Exception(String.format("Variable '%s' not found!", varName));
    }

    public static void throwUnknownDataType(String type) throws Exception{
        throw new Exception(String.format("Unknown data type '%s'!", type));
    }

    public static void throwRuntimeError(String msg) throws Exception{
        throw new Exception(msg);
    }

    public static void throwCastFailedError(String typeName) throws Exception{
        throw new Exception(String.format("Failed to cast to %s", typeName));
    }
}
