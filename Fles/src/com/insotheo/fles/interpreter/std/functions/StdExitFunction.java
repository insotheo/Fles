package com.insotheo.fles.interpreter.std.functions;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.variable.BlockReturn;
import com.insotheo.fles.interpreter.variable.FlesValue;
import com.insotheo.fles.interpreter.variable.FlesVariable;

import java.util.ArrayList;
import java.util.List;

public class StdExitFunction extends FlesFunction {

    public StdExitFunction() throws Exception{
        this.name = "std_exit";
        this.statements = new ArrayList<ASTNode>();
        this.parameters = new ArrayList<FlesVariable>();
        this.returnType = "void";
    }

    @Override
    public BlockReturn call(List<FlesValue> arguments) throws Exception {
        if(!arguments.isEmpty()){
            InterpreterExceptions.throwRuntimeError("std_exit doesn't accept any arguments!");
        }

        System.exit(0);

        clearParametersValues();
        return null;
    }
}
