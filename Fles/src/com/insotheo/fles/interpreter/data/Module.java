package com.insotheo.fles.interpreter.data;

import com.insotheo.fles.ast.BlockNode;

public class Module {
    public VariableStack variables;
    public FunctionStack functions;
    public ModuleStack submodules;

    public Module(){
        variables = new VariableStack();
        functions = new FunctionStack();
        submodules = new ModuleStack();
    }

    public void parse(BlockNode moduleBody){

    }
}
