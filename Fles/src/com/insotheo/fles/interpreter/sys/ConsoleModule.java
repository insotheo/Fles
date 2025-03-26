package com.insotheo.fles.interpreter.sys;

import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.blocks.FlesFunction;
import com.insotheo.fles.interpreter.data.*;
import com.insotheo.fles.interpreter.data.Module;

import java.util.List;

public class ConsoleModule extends Module {

    public ConsoleModule() throws Exception{
        super();
        this.functions.pushFunction("writeln", new SysConsoleWritelnFunction());
        this.functions.pushFunction("write", new SysConsoleWriteFunction());
        this.functions.pushFunction("exit", new SysConsoleExitFunction());
    }

}

class SysConsoleWritelnFunction extends FlesFunction{
    public SysConsoleWritelnFunction() throws Exception{
        VariableStack params = new VariableStack();
        params.pushVariable("__CONTENT__", new FlesVariable("void"));
        this.parameters = params;
        this.returnTypeName = "void";
        this.statements = null;
    }

    @Override
    public BlockReturn call(String functionName, List<FlesValue> arguments) throws Exception {
        if(arguments.isEmpty()){
            System.out.println();
            return null;
        } else if(arguments.size() != 1){
            InterpreterExceptions.throwRuntimeError("Console.writeln function takes only 1 or zero arguments!");
        }

        System.out.println(arguments.getFirst().getData().toString());

        clearParametersValues();
        return null;
    }
}

class SysConsoleWriteFunction extends FlesFunction{
    public SysConsoleWriteFunction() throws Exception{
        VariableStack params = new VariableStack();
        params.pushVariable("__CONTENT__", new FlesVariable("void"));
        this.parameters = params;
        this.returnTypeName = "void";
        this.statements = null;
    }

    @Override
    public BlockReturn call(String functionName, List<FlesValue> arguments) throws Exception {
        if (arguments.size() != 1) {
            InterpreterExceptions.throwRuntimeError("Console.write function always takes 1 argument!");
        }

        System.out.print(arguments.getFirst().getData().toString());

        clearParametersValues();
        return null;
    }
}

class SysConsoleExitFunction extends FlesFunction{
    public SysConsoleExitFunction() throws Exception{
        this.parameters = new VariableStack();
        this.returnTypeName = "void";
        this.statements = null;
    }

    @Override
    public BlockReturn call(String functionName, List<FlesValue> arguments) throws Exception {
        if (!arguments.isEmpty()) {
            InterpreterExceptions.throwRuntimeError("Console.exit doesn't accept any arguments!");
        }

        System.exit(0);

        clearParametersValues();
        return null;
    }
}