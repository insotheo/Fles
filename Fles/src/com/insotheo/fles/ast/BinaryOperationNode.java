package com.insotheo.fles.ast;

import com.insotheo.fles.parser.OperationValue;

public class BinaryOperationNode implements ASTNode {
    private final ASTNode leftNode;
    private final OperationValue operation;
    private final ASTNode rightNode;

    public BinaryOperationNode(ASTNode left, OperationValue op, ASTNode right) {
        leftNode = left;
        operation = op;
        rightNode = right;
    }

    public ASTNode getLeft() {
        return leftNode;
    }

    public OperationValue getOperation() {
        return operation;
    }

    public ASTNode getRight() {
        return rightNode;
    }
}
