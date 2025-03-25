package com.insotheo.fles.ast;

public class ModuleNode implements ASTNode{
    private final String moduleName;
    private final BlockNode body;

    public ModuleNode(String name, BlockNode body){
        this.moduleName = name;
        this.body = body;
    }

    public String getModuleName() {
        return moduleName;
    }

    public BlockNode getBody() {
        return body;
    }
}
