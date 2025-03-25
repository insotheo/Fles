package com.insotheo.fles.interpreter;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.interpreter.std.types.*;

import java.util.List;

public class FlesInterpreter {

    public FlesInterpreter(List<ASTNode> nodes) throws Exception {
        InterpreterData.init();

        //types
        InterpreterData.defineNewType("void", new VoidDataType());
        InterpreterData.defineNewType("int", new IntDataType());
        InterpreterData.defineNewType("float", new FloatDataType());
        InterpreterData.defineNewType("char", new CharDataType());
        InterpreterData.defineNewType("string", new StringDataType());
        InterpreterData.defineNewType("bool", new BoolDataType());


    }

    public void callMain() throws Exception {

    }
}
