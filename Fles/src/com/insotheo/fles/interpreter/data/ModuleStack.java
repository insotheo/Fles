package com.insotheo.fles.interpreter.data;

import com.insotheo.fles.interpreter.InterpreterExceptions;

import java.util.HashMap;
import java.util.Map;

public class ModuleStack {
    private final Map<String, Module> modules;

    public ModuleStack(){
        modules = new HashMap<>();
    }

    public void addModule(String name, Module module) throws Exception{
        if(!isModuleInStack(name)){
            InterpreterExceptions.throwRuntimeError(String.format("Module with name '%s' already defined!", name));
        }
        modules.put(name, module);
    }

    public boolean isModuleInStack(String name){
        return modules.containsKey(name);
    }

}
