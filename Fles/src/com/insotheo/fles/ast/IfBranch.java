package com.insotheo.fles.ast;

import java.util.List;

public class IfBranch implements ASTNode{
    private final List<ConditionBlock> branches;
    private final BlockNode elseBranch;

    public IfBranch(List<ConditionBlock> branches, BlockNode elseBranch){
        this.branches = branches;
        this.elseBranch = elseBranch;
    }

    public List<ConditionBlock> getBranches() {
        return branches;
    }

    public BlockNode getElseBranch() {
        return elseBranch;
    }
}
