package com.insotheo.fles.interpreter;

import com.insotheo.fles.ast.*;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.data.*;
import com.insotheo.fles.parser.DeleteTypeValue;

import java.util.List;
import java.util.ArrayList;

public class FlesEvaluate {

    public static FlesValue evalExpression(ASTNode node, VariableStack variables) throws Exception{
        if(node.getClass() == NumberNode.class){
            return new FlesValue(ValueType.Numeric, ((NumberNode) node).getValue());
        }
        else if(node.getClass() == StringLiteralNode.class){
            return new FlesValue(ValueType.StringLiteral, ((StringLiteralNode) node).getValue());
        }
        else if(node.getClass() == CharLiteral.class){
            return new FlesValue(ValueType.CharLiteral, ((CharLiteral) node).getValue());
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
            final String varName = var.getName();
            FlesValue value = null;
            String varType = "";

            if(variables.isVariableInStack(varName)){
                final FlesVariable variable = variables.getVariable(varName);
                value = variable.getValue();
                varType = variable.getTypeName();
            }
            else if(InterpreterData.isGlobalVariableAlreadyExist(varName)){
                final FlesVariable variable = InterpreterData.getVariable(varName);
                value = variable.getValue();
                varType = variable.getTypeName();
            }
            else{
                InterpreterExceptions.throwVariableNotFound(var.getName());
                return null;
            }
            
            return new FlesValue(varType, value.getData());
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

    public static BlockReturn evalBlock(List<ASTNode> nodes, VariableStack inputVariables) throws Exception{
        VariableStack variables = new VariableStack();
        variables.merge(inputVariables);

        for(ASTNode node : nodes){
            if(node.getClass() == FunctionCallNode.class){
                FunctionCallNode callNode = ((FunctionCallNode) node);
                evalFunctionCall(callNode, variables);
            }

            else if(node.getClass() == VariableNode.class){
                VariableNode varNode = ((VariableNode) node);
                FlesVariable newVariable = new FlesVariable(varNode.getType());
                variables = addVariable(variables, varNode.getName(), newVariable);
            }

            else if(node.getClass() == AssignmentNode.class){
                AssignmentNode assignmentNode = ((AssignmentNode) node);
                if(assignmentNode.getIsJustCreated()){
                    VariableNode newVarNode = assignmentNode.getVariable();
                    FlesVariable newVar = new FlesVariable(newVarNode.getType());
                    FlesValue newVarValue = evalExpression(assignmentNode.getValue(), variables);
                    newVar.setData(newVarValue.getData());
                    variables = addVariable(variables, newVarNode.getName(), newVar);
                }

                else{
                    String varName = assignmentNode.getVariable().getName();
                    FlesValue value = evalExpression(assignmentNode.getValue(), variables);

                    if(variables.isVariableInStack(varName)){
                        variables.setVariableValue(varName, value);
                    }
                    else if(InterpreterData.isGlobalVariableAlreadyExist(varName)){
                        InterpreterData.setGlobalVariableValue(varName, value);
                    }
                    else{
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

        variables.clearStack();
        return null;
    }

    public static BlockReturn evalFunction(FlesFunction function) throws Exception{
        return evalBlock(function.getStatements(), function.getParameters());
    }

    public static BlockReturn evalFunctionCall(FunctionCallNode callNode, VariableStack variables) throws Exception{
        List<FlesValue> arguments = new ArrayList<>();
        List<ASTNode> expressions = callNode.getArguments();
        for(ASTNode node : expressions){
            FlesValue val = evalExpression(node, variables);
            arguments.add(val);
        }
        return InterpreterData.callFunction(callNode.getName(), arguments);
    }

    private static VariableStack addVariable(VariableStack variables, String name, FlesVariable newVar) throws Exception{
        if(variables.isVariableInStack(name) || InterpreterData.isGlobalVariableAlreadyExist(name)){
            InterpreterExceptions.throwVariableIsAlreadyInStack(name);
        }
        variables.pushVariable(name, newVar);
        return variables;
    }

    private static VariableStack deleteVariable(VariableStack variables, String name) throws Exception{
        if(!variables.isVariableInStack(name)){
            InterpreterExceptions.throwRuntimeError(String.format("Can't delete variable '%s', because it doesn't exist!", name));
        }
        variables.popVariable(name);
        return variables;
    }

}
