package com.insotheo.fles.ast;

public class BinaryOperationNode implements ASTNode{
    private final ASTNode leftNode;
    private final String operation;
    private final ASTNode rightNode;

    public BinaryOperationNode(ASTNode left, String op, ASTNode right){
        leftNode = left;
        operation = op;
        rightNode = right;
    }

    public ASTNode getLeft(){
        return leftNode;
    }

    public String getOperation(){
        return operation;
    }

    public ASTNode getRight(){
        return rightNode;
    }
}
