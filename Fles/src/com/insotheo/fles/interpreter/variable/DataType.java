package com.insotheo.fles.interpreter.variable;

public class DataType {
    protected String typeName;

    public DataType(String name){
        typeName = name;
    }

    public String getName(){
        return typeName;
    }

    public static boolean isDataMatch(String value) throws Exception{
        return false;
    }

    public String inferValue(String value) throws Exception{
        return "";
    }

    public boolean isNumericType(){
        return false;
    }

    public boolean isChar(){
        return false;
    }

    public boolean isString(){
        return false;
    }
}
