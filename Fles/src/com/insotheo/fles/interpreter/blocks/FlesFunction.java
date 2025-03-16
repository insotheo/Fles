package com.insotheo.fles.interpreter.blocks;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.ast.ParameterNode;
import com.insotheo.fles.interpreter.FlesEvaluate;
import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.variable.DataTypeImpl;
import com.insotheo.fles.interpreter.variable.FlesValue;
import com.insotheo.fles.interpreter.variable.FlesVariable;

import java.util.ArrayList;
import java.util.List;

public class FlesFunction extends InterpreterBlock {
    protected List<FlesVariable> parameters;

    protected FlesFunction(){}

    public FlesFunction(String name, List<ASTNode> statements, List<ParameterNode> parameters) throws Exception{
        this.name = name;
        this.statements = statements;
        this.parameters = new ArrayList<>();
        for(ParameterNode node : parameters){
            this.parameters.add(new FlesVariable(
                    DataTypeImpl.parseDataType(node.getType()),
                    node.getName(),
                    ""
            ));
        }
    }

    public void call(List<FlesValue> arguments) throws Exception{
        if(arguments.size() != parameters.size()){
            InterpreterExceptions.throwRuntimeError(String.format("For function %s amount of gotten arguments is not equal to amount of parameters amount!", this.getName()));
        }
        for(int i = 0; i < parameters.size(); i++){
            parameters.get(i).setValue(arguments.get(i).getData(), arguments.get(i).getType());
        }

        FlesEvaluate.evalFunction(this);

        for(FlesVariable param : parameters){
            param.setValue(null);
        }
    }

    public List<FlesVariable> getParameters() {
        return parameters;
    }
}
