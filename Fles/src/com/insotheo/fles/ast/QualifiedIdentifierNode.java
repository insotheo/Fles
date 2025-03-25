package com.insotheo.fles.ast;

public class QualifiedIdentifierNode implements ASTNode{
    private final String qualifiedName;

    public QualifiedIdentifierNode(String qualifiedName){
        this.qualifiedName = qualifiedName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }
}
