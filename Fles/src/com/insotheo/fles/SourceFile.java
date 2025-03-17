package com.insotheo.fles;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.interpreter.FlesInterpreter;
import com.insotheo.fles.interpreter.InterpreterData;
import com.insotheo.fles.lexer.Lexer;
import com.insotheo.fles.parser.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SourceFile {
    private List<ASTNode> sourceNodes;
    private String fileName = "";
    private boolean isParsingSuccess = false;

    public SourceFile(String pathToFile){
        Lexer sourceLexer = null;
        Parser sourceParser = null;
        sourceNodes = null;
        try {
            Path filePath = Paths.get(pathToFile);
            fileName = filePath.getFileName().toString();

            if(!Files.exists(filePath)){
                throw new FileNotFoundException(String.format("File \"%s\" doesn't exist!", pathToFile));
            }
            if(!filePath.getFileName().toString().toLowerCase().endsWith(".fls")){
                throw new Exception("Provided file is not Fles file!");
            }
            String content = Files.readString(filePath);

            sourceLexer = new Lexer(content);
            sourceParser = new Parser(sourceLexer);
            sourceNodes = sourceParser.parse();

            isParsingSuccess = true;
        }
        catch(IOException e){
            System.err.println(String.format("Error reading the file: %s", e.getMessage()));
        }
        catch (Exception ex){
            System.err.println(String.format("Error at file \"%s\" at (%s): %s", fileName.toString(), sourceLexer.getPosition().toString(), ex.getMessage()));
        }
    }

    public void run(){
        try {
            FlesInterpreter instance = new FlesInterpreter(sourceNodes);
            instance.callMain();
        }
        catch (Exception ex){
            System.err.println(String.format("Fles runtime error: %s", ex.getMessage()));
        }
    }

    public String getFileName(){
        return fileName;
    }

    public boolean isParsingSuccess() {
        return isParsingSuccess;
    }
}
