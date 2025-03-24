package com.insotheo.fles.interpreter.data;

import com.insotheo.fles.interpreter.InterpreterExceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableStack {
    private final Map<String, FlesVariable> variables;

    public VariableStack() {
        variables = new HashMap<>();
    }

    public void pushVariable(String name, FlesVariable variable) throws Exception {
        if (isVariableInStack(name)) {
            InterpreterExceptions.throwVariableIsAlreadyInStack(name);
        }
        variables.put(name, variable);
    }

    public void popVariable(String name) throws Exception {
        if (!isVariableInStack(name)) {
            InterpreterExceptions.throwVariableNotFound(name);
        }
        variables.remove(name);
    }

    public void merge(VariableStack stack) throws Exception {
        for (String varName : stack.variables.keySet()) {
            pushVariable(varName, stack.getVariable(varName));
        }
    }

    public FlesVariable getVariable(String name) throws Exception {
        if (!isVariableInStack(name)) {
            InterpreterExceptions.throwVariableNotFound(name);
        }
        return variables.get(name);
    }

    public void setVariableValue(String name, FlesValue value) {
        variables.get(name).setValue(value);
    }

    public void setVariablesValues(List<FlesValue> values) throws Exception {
        if (values.size() != size()) {
            InterpreterExceptions.throwRuntimeError("Amount of values doesn't match to amount of variables in stack!");
        }

        int index = 0;
        for (FlesVariable variable : variables.values()) {
            variable.setValue(values.get(index));
            index++;
        }
    }

    public void clearStack() {
        variables.clear();
    }

    public void clearStackValues() {
        for (FlesVariable variable : variables.values()) {
            variable.setValue(null);
        }
    }

    public int size() {
        return variables.size();
    }

    public boolean isVariableInStack(String name) {
        return variables.containsKey(name);
    }

}
