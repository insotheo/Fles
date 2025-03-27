package com.insotheo.fles.ast;

public class WhileLoop implements ASTNode{
    private final ASTNode expression;
    private final BlockNode body;

    public WhileLoop(ASTNode expression, BlockNode body){
        this.expression = expression;
        this.body = body;
    }

    public ASTNode getExpression() {
        return expression;
    }

    public BlockNode getBody() {
        return body;
    }
}
