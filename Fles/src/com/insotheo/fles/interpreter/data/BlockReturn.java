package com.insotheo.fles.interpreter.data;

public class BlockReturn {
    private final FlesValue value;

    public BlockReturn(FlesValue val){
        value = val;
    }

    public FlesValue getReturnValue() {
        return value;
    }
}
