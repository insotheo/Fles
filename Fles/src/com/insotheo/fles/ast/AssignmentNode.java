package com.insotheo.fles.ast;

public class AssignmentNode implements ASTNode {
    private final VariableNode varNode;
    private final ASTNode value;
    private final boolean isJustCreated;

    public AssignmentNode(VariableNode var, ASTNode val, boolean justCreated) {
        varNode = var;
        value = val;
        isJustCreated = justCreated;
    }

    public VariableNode getVariable() {
        return varNode;
    }

    public ASTNode getValue() {
        return value;
    }

    public boolean getIsJustCreated() {
        return isJustCreated;
    }
}
