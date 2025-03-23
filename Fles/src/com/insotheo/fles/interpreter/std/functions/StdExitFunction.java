package com.insotheo.fles.interpreter.std.functions;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.data.BlockReturn;
import com.insotheo.fles.interpreter.data.FlesValue;
import com.insotheo.fles.interpreter.data.VariableStack;

import java.util.ArrayList;
import java.util.List;

public class StdExitFunction extends FlesFunction {

    public StdExitFunction() {
        this.statements = new ArrayList<ASTNode>();
        this.parameters = new VariableStack();
        this.returnTypeName = "void";
    }

    @Override
    public BlockReturn call(String name, List<FlesValue> arguments) throws Exception {
        if (!arguments.isEmpty()) {
            InterpreterExceptions.throwRuntimeError("std_exit doesn't accept any arguments!");
        }

        System.exit(0);

        clearParametersValues();
        return null;
    }
}
