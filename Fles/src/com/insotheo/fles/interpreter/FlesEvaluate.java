package com.insotheo.fles.interpreter;

import com.insotheo.fles.ast.*;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.variable.BlockReturn;
import com.insotheo.fles.interpreter.variable.FlesValue;
import com.insotheo.fles.interpreter.variable.FlesVariable;
import com.insotheo.fles.interpreter.variable.ValueType;
import com.insotheo.fles.parser.DeleteTypeValue;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class FlesEvaluate {

    public static FlesValue evalExpression(ASTNode node, List<FlesVariable> variables) throws Exception{
        if(node.getClass() == NumberNode.class){
            return new FlesValue(String.valueOf(((NumberNode) node).getValue()), ValueType.Numeric);
        }
        else if(node.getClass() == StringLiteralNode.class){
            return new FlesValue(((StringLiteralNode) node).getValue(), ValueType.StringLiteral);
        }
        else if(node.getClass() == CharLiteral.class){
            return new FlesValue(String.valueOf(((CharLiteral) node).getValue()), ValueType.CharLiteral);
        }

        else if(node.getClass() == FunctionCallNode.class){
            FunctionCallNode funcCallNode = ((FunctionCallNode) node);
            return evalFunctionCall(funcCallNode, variables).getReturnValue();
        }

        else if(node.getClass() == BlockNode.class){
            return evalBlock(((BlockNode) node).getStatements(), variables).getReturnValue();
        }

        else if(node.getClass() == VariableNode.class){
            VariableNode var = ((VariableNode) node);
            boolean varFound = false;
            String value = "";
            String varType = "";

            for(FlesVariable local : variables){
                if(local.getName().equals(var.getName())){
                    varType = local.getType().getName();
                    value = local.getValue().getData();
                    varFound = true;
                    break;
                }
            }
            if(!varFound && InterpreterData.isGlobalVariableAlreadyExist(var.getName())){
                varType = Objects.requireNonNull(InterpreterData.getVariable(var.getName())).getType().getName();
                value = Objects.requireNonNull(InterpreterData.getVariable(var.getName())).getValue().getData();
                varFound = true;
            }

            if(!varFound){
                InterpreterExceptions.throwVariableNotFound(var.getName());
                return null;
            }
            
            return new FlesValue(value, varType);
        }

        else if(node.getClass() == BinaryOperationNode.class){
            BinaryOperationNode binaryNode = ((BinaryOperationNode) node);
            FlesValue left = evalExpression(binaryNode.getLeft(), variables);
            FlesValue right = evalExpression(binaryNode.getRight(), variables);

            if(left == null || right == null){
                InterpreterExceptions.throwRuntimeError("Failed to make an operation: one of the operands is null!");
                return null;
            }

            switch (binaryNode.getOperation()){
                case Addition:
                    left.add(right);
                    return left;

                case Subtraction:
                    left.subtract(right);
                    return left;

                case Multiplication:
                    left.multiply(right);
                    return left;

                case Division:
                    left.divide(right);
                    return left;

                default: InterpreterExceptions.throwRuntimeError("Unknown operation!");
            }

        }

        InterpreterExceptions.throwRuntimeError("Parsing expression failure!");
        return null;
    }

    public static BlockReturn evalBlock(List<ASTNode> nodes, List<FlesVariable> inputVariables) throws Exception{
        List<FlesVariable> variables = new ArrayList<>(inputVariables);
        for(ASTNode node : nodes){
            if(node.getClass() == FunctionCallNode.class){
                FunctionCallNode callNode = ((FunctionCallNode) node);
                evalFunctionCall(callNode, variables);
            }

            else if(node.getClass() == VariableNode.class){
                VariableNode varNode = ((VariableNode) node);
                FlesVariable newVariable = new FlesVariable(varNode.getType(), varNode.getName());
                variables = addVariable(variables, newVariable);
            }

            else if(node.getClass() == AssignmentNode.class){
                AssignmentNode assignmentNode = ((AssignmentNode) node);
                if(assignmentNode.getIsJustCreated()){
                    VariableNode newVarNode = assignmentNode.getVariable();
                    FlesVariable newVar = new FlesVariable(newVarNode.getType(), newVarNode.getName());
                    FlesValue newVarValue = evalExpression(assignmentNode.getValue(), variables);
                    newVar.setData(newVarValue.getData());
                    variables = addVariable(variables, newVar);
                }

                else{
                    String varName = assignmentNode.getVariable().getName();
                    boolean varFound = false;
                    FlesValue value = evalExpression(assignmentNode.getValue(), variables);

                    for(FlesVariable var : variables){
                        if(var.getName().equals(varName)){
                            var.setValue(value.getData());
                            varFound = true;
                        }
                    }
                    if(!varFound && InterpreterData.isGlobalVariableAlreadyExist(varName)){
                        InterpreterData.setGlobalVariableValue(varName, value);
                        varFound = true;
                    }

                    if(!varFound){
                        InterpreterExceptions.throwRuntimeError(String.format("Can't do assignment because of there is no variable with name %s exist!", varName));
                        return null;
                    }

                }
            }

            else if(node.getClass() == ReturnNode.class){
                FlesValue value = evalExpression(((ReturnNode) node).getValue(), variables);
                return new BlockReturn(value);
            }

            else if(node.getClass() == DeleteNode.class){
                DeleteNode delNode = ((DeleteNode) node);
                switch (delNode.getDeleteType()){
                    case DeleteTypeValue.Variable:
                        variables = deleteVariable(variables, delNode.getName());
                        break;

                    case DeleteTypeValue.GlobalVariable:
                        InterpreterData.deleteGlobalVariable(delNode.getName());
                        break;
                }
            }
        }
        variables.clear();
        return null;
    }

    public static BlockReturn evalFunction(FlesFunction function) throws Exception{
        return evalBlock(function.getStatements(), function.getParameters());
    }

    public static BlockReturn evalFunctionCall(FunctionCallNode callNode, List<FlesVariable> variables) throws Exception{
        List<FlesValue> arguments = new ArrayList<>();
        List<ASTNode> expressions = callNode.getArguments();
        for(ASTNode node : expressions){
            FlesValue val = evalExpression(node, variables);
            arguments.add(val);
        }
        return InterpreterData.callFunction(callNode.getName(), arguments);
    }

    private static List<FlesVariable> addVariable(List<FlesVariable> variables, FlesVariable newVar) throws Exception{
        for(FlesVariable var : variables){
            if(var.getName().equals(newVar.getName())){
                InterpreterExceptions.throwRuntimeError(String.format("Variable with name '%s' already exists!", newVar.getName()));
            }
        }
        if(InterpreterData.isGlobalVariableAlreadyExist(newVar.getName())){
            InterpreterExceptions.throwRuntimeError(String.format("Variable with name '%s' already exists!", newVar.getName()));
        }
        variables.add(newVar);
        return variables;
    }

    private static List<FlesVariable> deleteVariable(List<FlesVariable> variables, String name) throws Exception{
        int index = -1;
        for(FlesVariable var : variables){
            if(var.getName().equals(name)){
                index = variables.indexOf(var);
            }
        }

        if(index == -1){
            InterpreterExceptions.throwRuntimeError(String.format("Can't delete variable '%s', because it doesn't exist!", name));
        }

        variables.remove(index);
        return variables;
    }

}
