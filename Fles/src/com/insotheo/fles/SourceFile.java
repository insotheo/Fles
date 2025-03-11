package com.insotheo.fles;

import com.insotheo.fles.ast.ASTNode;
import com.insotheo.fles.lexer.Lexer;
import com.insotheo.fles.parser.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SourceFile {
    public SourceFile(String pathToFile){
        try {
            Path filePath = Paths.get(pathToFile);

            if(!Files.exists(filePath)){
                throw new FileNotFoundException(String.format("File \"%s\" doesn't exist!", pathToFile));
            }
            if(!filePath.getFileName().toString().toLowerCase().endsWith(".fls")){
                throw new Exception("Provided file is not Fles file!");
            }
            String content = Files.readString(filePath);

            Lexer sourceLexer = new Lexer(content);
            Parser sourceParser = new Parser(sourceLexer);
            List<ASTNode> sourceNodes = sourceParser.parse();
        }
        catch(IOException e){
            System.err.println(String.format("Error reading the file: %s", e.getMessage()));
        }
        catch (Exception ex){
            System.err.println(String.format("Error at file \"%s\": %s", Paths.get(pathToFile).getFileName().toString(), ex.getMessage()));
        }
    }
}
