package com.insotheo.fles.ast;

public class CharLiteral implements ASTNode{
    private final char value;

    public CharLiteral(char literal){
        value = literal;
    }

    public char getValue(){
        return value;
    }

}
