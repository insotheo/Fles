package com.insotheo.fles.interpreter.std;

import com.insotheo.fles.interpreter.variable.FlesValue;

public class StdFunctionsImpl {
    public static void print(FlesValue value){
        System.out.print(value.getData());
    }

    public static void println(FlesValue value){
        System.out.println(value.getData());
    }

    public static void println(){
        System.out.println();
    }
}
