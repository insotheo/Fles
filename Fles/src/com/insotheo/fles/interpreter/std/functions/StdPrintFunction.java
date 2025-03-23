package com.insotheo.fles.interpreter.std.functions;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.data.BlockReturn;
import com.insotheo.fles.interpreter.data.FlesValue;
import com.insotheo.fles.interpreter.data.FlesVariable;
import com.insotheo.fles.interpreter.data.VariableStack;

import java.util.ArrayList;
import java.util.List;

public class StdPrintFunction extends FlesFunction {

    public StdPrintFunction() throws Exception {
        VariableStack params = new VariableStack();
        params.pushVariable("value", new FlesVariable("void"));
        this.statements = new ArrayList<>();
        this.parameters = params;
        this.returnTypeName = "void";
    }

    @Override
    public BlockReturn call(String name, List<FlesValue> arguments) throws Exception {
        if (arguments.size() != 1) {
            InterpreterExceptions.throwRuntimeError("std_print function takes only 1 argument!");
        }

        System.out.print(arguments.get(0).getData().toString());

        clearParametersValues();
        return null;
    }
}
