package com.insotheo.fles.interpreter.data;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.blocks.FlesFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionStack {
    private final Map<String, FlesFunction> functions;

    public FunctionStack() {
        functions = new HashMap<>();
    }

    public void pushFunction(String name, FlesFunction function) throws Exception {
        if (isFunctionInStack(name)) {
            InterpreterExceptions.throwFunctionIsAlreadyInStack(name);
        }
        functions.put(name, function);
    }

    public BlockReturn callFunction(String name, List<FlesValue> arguments) throws Exception {
        if (!isFunctionInStack(name)) {
            InterpreterExceptions.throwFunctionNotFound(name);
        }
        BlockReturn returnValue = functions.get(name).call(name, arguments);
        if (functions.get(name).getReturnTypeName().equals("void")) {
            return null;
        }
        return returnValue;
    }

    public int size() {
        return functions.size();
    }

    public boolean isFunctionInStack(String name) {
        return functions.containsKey(name);
    }
}
