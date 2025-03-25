package com.insotheo.fles.interpreter;

import com.insotheo.fles.interpreter.data.DataType;
import com.insotheo.fles.interpreter.data.FlesValue;
import com.insotheo.fles.interpreter.data.Module;
import com.insotheo.fles.interpreter.data.ModuleStack;

import java.util.HashMap;
import java.util.Map;

public class InterpreterData {
    public static Map<String, DataType> dataTypes = null;
    public static Module zeroModule = null;
    public static ModuleStack modules = null;

    public static void init() {
        dataTypes = new HashMap<>();
        zeroModule = new Module();
        modules = new ModuleStack();
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
}
