package com.insotheo.fles.interpreter;

import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.variable.FlesValue;
import com.insotheo.fles.interpreter.variable.FlesVariable;

import java.util.ArrayList;
import java.util.List;

public class InterpreterData {
    public static List<FlesFunction> functions = new ArrayList<FlesFunction>();
    public static List<FlesVariable> globalVariables = new ArrayList<FlesVariable>();

    public static void callFunction(String name, List<FlesValue> values) throws Exception{
        for(FlesFunction function : functions){
            if(function.getName().equals(name)){
                function.call(values);
                return;
            }
        }
        InterpreterExceptions.throwNoFunctionExists(name);
    }

    public static FlesVariable getVariable(String name) throws Exception{
        for(FlesVariable var : globalVariables){
            if(var.getName().equals(name)){
                return var;
            }
        }
        InterpreterExceptions.throwNoVariableExists(name);
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

    public static void setGlobalVariableValue(String variableName, FlesValue value){
        for(FlesVariable var : globalVariables){
            if(var.getName().equals(variableName)){
                var.setValue(value.getData(), value.getType());
            }
        }
    }
}
