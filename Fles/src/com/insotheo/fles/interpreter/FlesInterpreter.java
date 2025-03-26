package com.insotheo.fles.interpreter;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.ast.BlockNode;
import com.insotheo.fles.interpreter.data.types.*;

import java.util.ArrayList;
import java.util.List;

public class FlesInterpreter {

    public FlesInterpreter(List<ASTNode> nodes) throws Exception {
        InterpreterData.init();

        //primitive types
        InterpreterData.defineNewType("void", new VoidDataType());
        InterpreterData.defineNewType("int", new IntDataType());
        InterpreterData.defineNewType("float", new FloatDataType());
        InterpreterData.defineNewType("char", new CharDataType());
        InterpreterData.defineNewType("string", new StringDataType());
        InterpreterData.defineNewType("bool", new BoolDataType());

        //making zero module
        BlockNode zeroModuleBlock = new BlockNode(nodes);
        InterpreterData.zeroModule.parse(zeroModuleBlock);
    }

    public void callMain() throws Exception {
        if (!InterpreterData.isFunctionInModule("main")) {
            InterpreterExceptions.throwMainFunctionNotFound();
        }
        InterpreterData.callFunction(null, "main", new ArrayList<>());
    }
}
