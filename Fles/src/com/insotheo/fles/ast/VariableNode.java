package com.insotheo.fles.ast;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.variable.DataType;
import com.insotheo.fles.interpreter.variable.DataTypeImpl;

public class VariableNode implements ASTNode{
    private final String name;
    private final String type;

    public VariableNode(String type, String name){
        this.type = type;
        this.name = name;
    }

    public String getType(){
        return type;
    }

    public DataType getDataType() throws Exception{
        return DataTypeImpl.parseDataType(type);
    }

    public String getName(){
        return name;
    }
}
