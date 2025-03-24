package com.insotheo.fles.interpreter;

import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.data.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterpreterData {
    public static FunctionStack functions = new FunctionStack();
    public static VariableStack globalVariables = new VariableStack();
    public static Map<String, DataType> dataTypes = new HashMap<>();

    /// FUNCTIONS

    public static BlockReturn callFunction(String name, List<FlesValue> arguments) throws Exception {
        return functions.callFunction(name, arguments);
    }

    public static void addFunction(String name, FlesFunction function) throws Exception {
        functions.pushFunction(name, function);
    }

    public static boolean isFunctionExist(String name) {
        return functions.isFunctionInStack(name);
    }

    /// DATA TYPES

    public static void defineNewType(String name, DataType type) throws Exception {
        if (isTypeDefined(name)) {
            InterpreterExceptions.throwRuntimeError(String.format("Type with name '%s' is already defined!", name));
            return;
        }
        dataTypes.put(name, type);
    }

    public static DataType getDataType(String name) throws Exception {
        if (!isTypeDefined(name)) {
            InterpreterExceptions.throwUnknownDataType(name);
        }
        return dataTypes.get(name);
    }

    public static boolean isTypeDefined(String name) {
        if (name.equals("auto")) {
            return true;
        }
        return dataTypes.containsKey(name);
    }

    public static String findAutoType(FlesValue value) throws Exception {
        for (String typeName : dataTypes.keySet()) {
            dataTypes.get(typeName);
            if (DataType.isDataMatch(value.getData())) {
                return typeName;
            }
        }
        InterpreterExceptions.throwRuntimeError("No type instead of 'auto' found!");
        return null;
    }

    /// GLOBAL VARIABLES

    public static FlesVariable getVariable(String name) throws Exception {
        return globalVariables.getVariable(name);
    }

    public static void addGlobalVariable(String name, FlesVariable var) throws Exception {
        globalVariables.pushVariable(name, var);
    }

    public static boolean isGlobalVariableAlreadyExist(String name) {
        return globalVariables.isVariableInStack(name);
    }

    public static void setGlobalVariableValue(String name, FlesValue value) throws Exception {
        globalVariables.setVariableValue(name, value);
    }

    public static void deleteGlobalVariable(String name) throws Exception {
        globalVariables.popVariable(name);
    }
}
