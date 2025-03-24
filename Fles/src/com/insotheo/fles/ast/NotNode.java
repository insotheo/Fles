package com.insotheo.fles.ast;

public class NotNode implements ASTNode {
    private final ASTNode expression;

    public NotNode(ASTNode expression) {
        this.expression = expression;
    }

    public ASTNode getExpression() {
        return expression;
    }
}
