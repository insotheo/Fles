package com.insotheo.fles.interpreter.blocks;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.ast.ParameterNode;
import com.insotheo.fles.interpreter.FlesEvaluate;
import com.insotheo.fles.interpreter.InterpreterData;
import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.data.*;
import com.insotheo.fles.interpreter.data.Module;

import java.util.List;

public class FlesFunction extends InterpreterBlock {
    protected VariableStack parameters;
    protected String returnTypeName;
    private Module module;

    protected FlesFunction() {
    }

    public FlesFunction(Module mod, List<ASTNode> statements, List<ParameterNode> parameters, String returnTypeName) throws Exception {
        this.statements = statements;
        this.parameters = new VariableStack();
        this.module = mod;

        if (!InterpreterData.isTypeDefined(returnTypeName)) {
            InterpreterExceptions.throwUnknownDataType(returnTypeName);
        }
        this.returnTypeName = returnTypeName;

        for (ParameterNode node : parameters) {
            if (!InterpreterData.isTypeDefined(node.getType())) {
                InterpreterExceptions.throwUnknownDataType(returnTypeName);
            }
            this.parameters.pushVariable(node.getName(), new FlesVariable(node.getType()));
        }
    }

    public BlockReturn call(String functionName, List<FlesValue> arguments) throws Exception {
        if (arguments.size() != parameters.size()) {
            InterpreterExceptions.throwRuntimeError(String.format("For function '%s' amount of gotten arguments doesn't match to amount of parameters amount!", functionName));
        }
        parameters.setVariablesValues(arguments);

        BlockReturn returnValue = FlesEvaluate.evalFunction(this);

        clearParametersValues();
        return returnValue;
    }

    public VariableStack getParameters() {
        return parameters;
    }

    public String getReturnTypeName() {
        return returnTypeName;
    }

    public void clearParametersValues() throws Exception {
        parameters.clearStackValues();
    }

    public Module getModule() {
        return module;
    }
}
