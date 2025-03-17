package com.insotheo.fles.ast;

public class VariableNode implements ASTNode{
    private final String name;
    private final String type;

    public VariableNode(String type, String name){
        this.type = type;
        this.name = name;
    }

    public String getType(){
        return type;
    }

    public String getName(){
        return name;
    }
}
