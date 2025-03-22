package com.insotheo.fles.ast;

import com.insotheo.fles.parser.DeleteTypeValue;

public class DeleteNode implements ASTNode{
    private final String name;
    private final DeleteTypeValue deleteType;

    public DeleteNode(String name, DeleteTypeValue deleteType){
        this.name = name;
        this.deleteType = deleteType;
    }

    public String getName() {
        return name;
    }

    public DeleteTypeValue getDeleteType() {
        return deleteType;
    }
}
