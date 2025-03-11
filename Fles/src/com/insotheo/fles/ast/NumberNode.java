package com.insotheo.fles.ast;

public class NumberNode implements ASTNode {
    private final double value;

    public NumberNode(double number){
        value = number;
    }

    public double getValue(){
        return value;
    }
}
