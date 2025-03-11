package com.insotheo.fles.ast;

import java.util.List;

public class FunctionNode implements ASTNode{
    private final String name;
    private final String returnType;
    private final List<ParameterNode> parameters;
    private final BlockNode body;

    public FunctionNode(String name, String returnType, List<ParameterNode> parameters, BlockNode body){
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName(){
        return name;
    }

    public String getReturnType(){
        return returnType;
    }

    public List<ParameterNode> getParameters(){
        return parameters;
    }

    public BlockNode getBody(){
        return body;
    }
}
