package com.insotheo.fles.interpreter.std.functions;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.variable.BlockReturn;
import com.insotheo.fles.interpreter.variable.FlesValue;
import com.insotheo.fles.interpreter.variable.FlesVariable;

import java.util.ArrayList;
import java.util.List;

public class StdPrintFunction extends FlesFunction {

    public StdPrintFunction() throws Exception{
        List<FlesVariable> params = new ArrayList<FlesVariable>();
        params.add(new FlesVariable("void", "value", ""));
        this.name = "std_print";
        this.statements = new ArrayList<>();
        this.parameters = params;
        this.returnType = "void";
    }

    @Override
    public BlockReturn call(List<FlesValue> arguments) throws Exception {
        if(arguments.size() != 1){
            InterpreterExceptions.throwRuntimeError("std_print function takes only 1 argument!");
        }

        System.out.print(arguments.get(0).getData());

        for(FlesVariable param : parameters){
            param.setValue("");
        }
        return null;
    }
}
