package com.insotheo.fles.ast;

public class StringLiteralNode implements ASTNode{
    private final String value;

    public StringLiteralNode(String literal){
        value = literal;
    }

    public String getValue(){
        return value;
    }
}
