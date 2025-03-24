package com.insotheo.fles.interpreter;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.ast.AssignmentNode;
import com.insotheo.fles.ast.FunctionNode;
import com.insotheo.fles.ast.VariableNode;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.data.FlesValue;
import com.insotheo.fles.interpreter.data.FlesVariable;
import com.insotheo.fles.interpreter.std.functions.StdExitFunction;
import com.insotheo.fles.interpreter.std.functions.StdPrintFunction;
import com.insotheo.fles.interpreter.std.functions.StdPrintlnFunction;
import com.insotheo.fles.interpreter.std.types.*;

import java.util.ArrayList;
import java.util.List;

public class FlesInterpreter {

    public FlesInterpreter(List<ASTNode> nodes) throws Exception {
        //adding std

        //types
        InterpreterData.defineNewType("void", new VoidDataType());
        InterpreterData.defineNewType("int", new IntDataType());
        InterpreterData.defineNewType("float", new FloatDataType());
        InterpreterData.defineNewType("char", new CharDataType());
        InterpreterData.defineNewType("string", new StringDataType());
        InterpreterData.defineNewType("bool", new BoolDataType());

        //functions
        InterpreterData.addFunction("std_print", new StdPrintFunction());
        InterpreterData.addFunction("std_println", new StdPrintlnFunction());
        InterpreterData.addFunction("std_exit", new StdExitFunction());

        //....

        for (ASTNode node : nodes) { //parsing functions, structs, enums
            if (node.getClass() == FunctionNode.class) {
                FunctionNode func = ((FunctionNode) node);
                InterpreterData.addFunction(func.getName(), new FlesFunction(func.getBody().getStatements(), func.getParameters(), func.getReturnType()));
            }
        }

        for (ASTNode node : nodes) {//parsing global variables
            if (node.getClass() == VariableNode.class) {
                VariableNode varNode = ((VariableNode) node);
                FlesVariable newVariable = new FlesVariable(varNode.getType());
                InterpreterData.addGlobalVariable(varNode.getName(), newVariable);
            } else if (node.getClass() == AssignmentNode.class) {
                AssignmentNode assignmentNode = ((AssignmentNode) node);
                if (assignmentNode.getIsJustCreated()) {
                    VariableNode newVarNode = assignmentNode.getVariable();
                    FlesVariable newVar = new FlesVariable(newVarNode.getType());
                    FlesValue newVarValue = FlesEvaluate.evalExpression(assignmentNode.getValue(), InterpreterData.globalVariables);
                    newVar.setData(newVarValue.getData());
                    InterpreterData.addGlobalVariable(newVarNode.getName(), newVar);
                } else {
                    InterpreterExceptions.throwRuntimeError("Can't do assignment out of the function!");
                }
            }
        }


    }

    public void callMain() throws Exception {
        if (!InterpreterData.isFunctionExist("main")) {
            InterpreterExceptions.throwMainFunctionNotFound();
        }
        InterpreterData.callFunction("main", new ArrayList<>());
    }
}
