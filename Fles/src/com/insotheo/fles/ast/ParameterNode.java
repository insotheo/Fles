package com.insotheo.fles.ast;

public class ParameterNode implements ASTNode {
    private final String name;
    private final String type;

    public ParameterNode(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
