package com.insotheo.fles.interpreter.variable;

public class BlockReturn {
    private final FlesValue value;

    public BlockReturn(FlesValue val){
        value = val;
    }

    public FlesValue getReturnValue() {
        return value;
    }
}
