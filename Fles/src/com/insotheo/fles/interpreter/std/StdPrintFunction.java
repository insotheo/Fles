package com.insotheo.fles.interpreter.std;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.variable.DataType;
import com.insotheo.fles.interpreter.variable.FlesValue;
import com.insotheo.fles.interpreter.variable.FlesVariable;

import java.util.ArrayList;
import java.util.List;

public class StdPrintFunction extends FlesFunction {

    public StdPrintFunction(){
        List<FlesVariable> params = new ArrayList<FlesVariable>();
        params.add(new FlesVariable(DataType.Auto, "value", ""));
        this.name = "std_print";
        this.statements = new ArrayList<>();
        this.parameters = params;
    }

    @Override
    public void call(List<FlesValue> arguments) throws Exception {
        if(arguments.size() != 1){
            InterpreterExceptions.throwRuntimeError("std_print function takes only 1 argument!");
        }

        StdFunctionsImpl.print(arguments.get(0));
    }
}
