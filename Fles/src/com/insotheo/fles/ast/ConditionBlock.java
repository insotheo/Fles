package com.insotheo.fles.ast;

public class ConditionBlock implements ASTNode{ //if, else if
    private final ASTNode expression;
    private final BlockNode body;

    public ConditionBlock(ASTNode condition, BlockNode body){
        this.expression = condition;
        this.body = body;
    }

    public ASTNode getExpression() {
        return expression;
    }

    public BlockNode getBody() {
        return body;
    }
}
