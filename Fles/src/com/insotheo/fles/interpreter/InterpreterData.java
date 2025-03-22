package com.insotheo.fles.interpreter;

import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.variable.*;

import java.util.ArrayList;
import java.util.List;

public class InterpreterData {
    public static List<FlesFunction> functions = new ArrayList<FlesFunction>();
    public static List<FlesVariable> globalVariables = new ArrayList<FlesVariable>();
    public static List<DataType> dataTypes = new ArrayList<DataType>();

    ///
    /// FUNCTIONS
    ///

    public static BlockReturn callFunction(String name, List<FlesValue> values) throws Exception{
        for(FlesFunction function : functions){
            if(function.getName().equals(name)){
                BlockReturn returnValue = function.call(values);
                if(function.getReturnType().equals("void")){
                    return null;
                }
                return returnValue;
            }
        }
        InterpreterExceptions.throwFunctionNotFound(name);
        return null;
    }

    public static FlesVariable getVariable(String name) throws Exception{
        for(FlesVariable var : globalVariables){
            if(var.getName().equals(name)){
                return var;
            }
        }
        InterpreterExceptions.throwVariableNotFound(name);
        return null;
    }

    public static void addFunction(FlesFunction function) throws Exception{
        for(FlesFunction func : functions){
            if(func.getName().equals(function.getName())){
                InterpreterExceptions.throwRuntimeError(String.format("Function with the name %s already exists!", func.getName()));
                return;
            }
        }
        functions.add(function);
    }

    ///
    /// DATA TYPES
    ///


    public static void defineNewType(DataType type) throws Exception{
        if(isTypeDefined(type.getName())){
            InterpreterExceptions.throwRuntimeError(String.format("Type with name %s is already defined!", type.getName()));
            return;
        }
        dataTypes.add(type);
    }

    public static DataType getDataType(String name) throws Exception{
        for(DataType type : dataTypes){
            if(type.getName().equals(name)){
                return type;
            }
        }
        InterpreterExceptions.throwUnknownDataType(name);
        return null;
    }

    public static boolean isTypeDefined(String name){
        if(name.equals("auto")){
            return true;
        }
        for(DataType type : dataTypes){
            if(type.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public static String findAutoType(String data) throws Exception{
        for(DataType type : dataTypes){
            if(type.isDataMatch(data)){
                return type.getName();
            }
        }
        InterpreterExceptions.throwRuntimeError("No matches for type auto found!");
        return null;
    }

    ///
    /// GLOBAL VARIABLES
    ///

    public static void addGlobalVariable(FlesVariable glob) throws Exception{
        if(isGlobalVariableAlreadyExist(glob.getName())){
            InterpreterExceptions.throwRuntimeError(String.format("Variable with the name \"%s\" already exists!", glob.getName()));
            return;
        }
        globalVariables.add(glob);
    }

    public static boolean isGlobalVariableAlreadyExist(String variableName){
        for(FlesVariable var : globalVariables){
            if(var.getName().equals(variableName)){
                return true;
            }
        }
        return false;
    }

    public static void setGlobalVariableValue(String variableName, FlesValue value) throws Exception{
        for(FlesVariable var : globalVariables){
            if(var.getName().equals(variableName)){
                var.setValue(value.getData());
            }
        }
    }

    public static void deleteGlobalVariable(String variableName) throws Exception{
        if(!isGlobalVariableAlreadyExist(variableName)){
            InterpreterExceptions.throwRuntimeError(String.format("Can't delete global variable '%s', because it doesn't exist!", variableName));
        }
        int index = -1;
        for(FlesVariable var : globalVariables){
            if(var.getName().equals(variableName)){
                index = globalVariables.indexOf(var);
                var = null;
            }
        }
        globalVariables.remove(index);
    }
}
