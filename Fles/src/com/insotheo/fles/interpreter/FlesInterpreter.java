package com.insotheo.fles.interpreter;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.ast.FunctionNode;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.std.StdPrintFunction;
import com.insotheo.fles.interpreter.std.StdPrintlnFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlesInterpreter {

    public FlesInterpreter(List<ASTNode> nodes) throws Exception{
        InterpreterData.addFunction(new StdPrintFunction());
        InterpreterData.addFunction(new StdPrintlnFunction());

        for (ASTNode node : nodes) {
            if (node.getClass() == FunctionNode.class) {
                FunctionNode func = ((FunctionNode) node);
                InterpreterData.addFunction(new FlesFunction(func.getName(), func.getBody().getStatements(), func.getParameters()));
            }
        }
    }

    public void callMain() throws Exception{
        for(FlesFunction func : InterpreterData.functions){
            if(Objects.equals(func.getName(), "main")){
                func.call(new ArrayList<>());
                return;
            }
        }
        InterpreterExceptions.throwMainFunctionNotFound();
    }
}
