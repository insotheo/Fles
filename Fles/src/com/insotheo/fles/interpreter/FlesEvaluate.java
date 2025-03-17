package com.insotheo.fles.interpreter;

import com.insotheo.fles.ast.*;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.variable.FlesValue;
import com.insotheo.fles.interpreter.variable.FlesVariable;
import com.insotheo.fles.interpreter.variable.ValueType;

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
                InterpreterExceptions.throwRuntimeError(String.format("Variable %s not found!", var.getName()));
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

    public static void evalBlock(List<ASTNode> nodes, List<FlesVariable> inputVariables) throws Exception{
        List<FlesVariable> variables = new ArrayList<>(inputVariables);
        for(ASTNode node : nodes){
            if(node.getClass() == FunctionCallNode.class){
                FunctionCallNode callNode = ((FunctionCallNode) node);
                evalFunctionCall(callNode, variables);
            }

            else if(node.getClass() == VariableNode.class){
                VariableNode varNode = ((VariableNode) node);
                FlesVariable newVariable = new FlesVariable(varNode.getType(), varNode.getName());
                variables.add(newVariable);
            }

            else if(node.getClass() == AssignmentNode.class){
                AssignmentNode assignmentNode = ((AssignmentNode) node);
                if(assignmentNode.getIsJustCreated()){
                    VariableNode newVarNode = assignmentNode.getVariable();
                    FlesVariable newVar = new FlesVariable(newVarNode.getType(), newVarNode.getName());
                    FlesValue newVarValue = evalExpression(assignmentNode.getValue(), variables);
                    newVar.setData(newVarValue.getData());
                    variables.add(newVar);
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
                        return;
                    }

                }
            }
        }
        variables.clear();
    }

    public static void evalFunction(FlesFunction function) throws Exception{
        evalBlock(function.getStatements(), function.getParameters());
    }

    public static void evalFunctionCall(FunctionCallNode callNode, List<FlesVariable> variables) throws Exception{
        List<FlesValue> arguments = new ArrayList<>();
        List<ASTNode> expressions = callNode.getArguments();
        for(ASTNode node : expressions){
            FlesValue val = evalExpression(node, variables);
            arguments.add(val);
        }
        InterpreterData.callFunction(callNode.getName(), arguments);
    }

}
