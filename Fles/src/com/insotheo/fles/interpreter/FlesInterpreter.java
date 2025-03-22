package com.insotheo.fles.interpreter;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.ast.AssignmentNode;
import com.insotheo.fles.ast.FunctionNode;
import com.insotheo.fles.ast.VariableNode;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.std.functions.StdExitFunction;
import com.insotheo.fles.interpreter.std.functions.StdPrintFunction;
import com.insotheo.fles.interpreter.std.functions.StdPrintlnFunction;
import com.insotheo.fles.interpreter.std.types.*;
import com.insotheo.fles.interpreter.variable.FlesValue;
import com.insotheo.fles.interpreter.variable.FlesVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlesInterpreter {

    public FlesInterpreter(List<ASTNode> nodes) throws Exception{
        //adding std

        //types
        InterpreterData.defineNewType(new VoidDataType());
        InterpreterData.defineNewType(new IntDataType());
        InterpreterData.defineNewType(new FloatDataType());
        InterpreterData.defineNewType(new CharDataType());
        InterpreterData.defineNewType(new StringDataType());

        //functions
        InterpreterData.addFunction(new StdPrintFunction());
        InterpreterData.addFunction(new StdPrintlnFunction());
        InterpreterData.addFunction(new StdExitFunction());

        //....

        for (ASTNode node : nodes) { //parsing function, structs, enums
            if (node.getClass() == FunctionNode.class) {
                FunctionNode func = ((FunctionNode) node);
                InterpreterData.addFunction(new FlesFunction(func.getName(), func.getBody().getStatements(), func.getParameters(), func.getReturnType()));
            }
        }

        for(ASTNode node : nodes){//parsing global variables
            if(node.getClass() == VariableNode.class){
                VariableNode varNode = ((VariableNode) node);
                FlesVariable newVariable = new FlesVariable(varNode.getType(), varNode.getName());
                InterpreterData.addGlobalVariable(newVariable);
            }

            else if(node.getClass() == AssignmentNode.class){
                AssignmentNode assignmentNode = ((AssignmentNode) node);
                if(assignmentNode.getIsJustCreated()){
                    VariableNode newVarNode = assignmentNode.getVariable();
                    FlesVariable newVar = new FlesVariable(newVarNode.getType(), newVarNode.getName());
                    FlesValue newVarValue = FlesEvaluate.evalExpression(assignmentNode.getValue(), InterpreterData.globalVariables);
                    newVar.setData(newVarValue.getData());
                    InterpreterData.addGlobalVariable(newVar);
                }
                else{
                    InterpreterExceptions.throwRuntimeError("Can't do assignment out of the function!");
                }
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
