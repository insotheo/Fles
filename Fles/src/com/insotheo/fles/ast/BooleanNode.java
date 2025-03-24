package com.insotheo.fles.ast;

public class BooleanNode implements ASTNode {
    private final boolean value;

    public BooleanNode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
