package com.insotheo.fles.interpreter;

public class InterpreterExceptions{
    public static void throwMainFunctionNotFound() throws Exception{
        throw new Exception("main() function was not found!");
    }

    public static void throwNoFunctionExists(String funcName) throws Exception{
        throw new Exception(String.format("No function %s exists!", funcName));
    }

    public static void throwNoVariableExists(String varName) throws Exception{
        throw new Exception(String.format("No variable %s exists!", varName));
    }

    public static void throwUnknownDataType(String type) throws Exception{
        throw new Exception(String.format("Unknown data type %s!", type));
    }

    public static void throwRuntimeError(String msg) throws Exception{
        throw new Exception(msg);
    }

    public static void throwCastFailedError(String typeName) throws Exception{
        throw new Exception(String.format("Failed to cast to %s", typeName));
    }
}
