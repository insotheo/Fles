package com.insotheo.fles.interpreter.blocks;

import com.insotheo.fles.ast.ASTNode;

import java.util.List;

public class InterpreterBlock {
    protected String name;
    protected List<ASTNode> statements;

    public String getName() {
        return name;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }
}
