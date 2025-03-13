package com.insotheo.fles.ast;

public class ReturnNode implements ASTNode{
    private final ASTNode returnNodeValue;

    public ReturnNode(ASTNode value){
        returnNodeValue = value;
    }

    public ASTNode getValue() {
        return returnNodeValue;
    }
}
