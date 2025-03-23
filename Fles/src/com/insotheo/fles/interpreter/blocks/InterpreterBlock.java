package com.insotheo.fles.interpreter.blocks;

import com.insotheo.fles.ast.ASTNode;

import java.util.List;

public class InterpreterBlock {
    protected List<ASTNode> statements;

    public List<ASTNode> getStatements() {
        return statements;
    }
}
