package com.insotheo.fles.interpreter.data;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.ast.BlockNode;
import com.insotheo.fles.ast.FunctionNode;
import com.insotheo.fles.ast.ModuleNode;
import com.insotheo.fles.interpreter.InterpreterData;
import com.insotheo.fles.interpreter.InterpreterExceptions;
import com.insotheo.fles.interpreter.blocks.FlesFunction;

import java.util.List;

public class Module {
    public FunctionStack functions;
    public ModuleStack submodules;

    public Module(){
        functions = new FunctionStack();
        submodules = new ModuleStack();
    }

    public void parse(BlockNode moduleBody) throws Exception {
        for (ASTNode node : moduleBody.getStatements()) {
            if (node.getClass() == ModuleNode.class) {
                Module newModule = new Module();
                newModule.parse(((ModuleNode) node).getBody());
                submodules.addModule(((ModuleNode) node).getModuleName(), newModule);
            } else if (node.getClass() == FunctionNode.class) {
                FunctionNode functionNode = ((FunctionNode) node);
                FlesFunction function = new FlesFunction(this, functionNode.getBody().getStatements(), functionNode.getParameters(), functionNode.getReturnType());
                functions.pushFunction(functionNode.getName(), function);
            }
        }
    }

    public boolean isFunctionInModule(String name) {
        if (functions.isFunctionInStack(name)) {
            return true;
        }

        for (Module submodule : submodules.getModules()) {
            if (submodule.functions.isFunctionInStack(name)) {
                return true;
            }
        }

        return false;
    }

    public BlockReturn findAndCallFunction(String name, List<FlesValue> args) throws Exception {
        String[] identifiers = name.split("\\.");
        Module current = this;

        if (identifiers.length == 1) {
            if (functions.isFunctionInStack(name)) {
                return functions.callFunction(name, args);
            }
        } else if (identifiers.length > 1) {
            for (int i = 0; i < identifiers.length; i++) {
                String identifier = identifiers[i];

                if (i == identifiers.length - 1) {
                    if (current.isFunctionInModule(identifier)) {
                        return current.functions.callFunction(identifier, args);
                    } else {
                        if (InterpreterData.zeroModule.isFunctionInModule(name)) {
                            return InterpreterData.callFunction(null, name, args);
                        }
                        InterpreterExceptions.throwFunctionNotFound(name);
                    }
                } else {
                    current = current.submodules.getModule(identifier, true);
                    if (current == null) {
                        current = InterpreterData.zeroModule.submodules.getModule(identifier, true);
                        if (current == null) {
                            break;
                        }
                    }
                }
            }
        }

        InterpreterExceptions.throwFunctionNotFound(name);
        return null;
    }

}
