package com.insotheo.fles.parser;

import java.util.ArrayList;
import java.util.List;

import com.insotheo.fles.ast.*;
import com.insotheo.fles.lexer.Lexer;
import com.insotheo.fles.lexer.Token;
import com.insotheo.fles.lexer.TokenType;

public class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private List<ASTNode> nodes;

    public Parser(Lexer fileLexer) throws Exception{
        lexer = fileLexer;
        currentToken = lexer.next();
        nodes = new ArrayList<ASTNode>();
    }

    public List<ASTNode> parse() throws Exception{
        while(currentToken != null && currentToken.type != TokenType.EOF){
            nodes.add(parseStatement());
        }
        return nodes;
    }

    private ASTNode parseStatement() throws Exception{
        if(eat(TokenType.Let)){ //variable definition
            //let <identifier> : <data_type> ...
            fatalCheck(TokenType.Identifier);
            String identifier = currentToken.value;
            eatFatal(TokenType.Identifier);

            eatFatal(TokenType.Colon);
            fatalCheck(TokenType.Identifier);
            String type = currentToken.value;
            eatFatal(TokenType.Identifier);

            VariableNode variable = new VariableNode(type, identifier);
            if(eat(TokenType.EqualSign)){ //assignment: let <identifier> : <type> = <expression>;
                ASTNode expression = parseExpression();
                eatFatal(TokenType.Semicolon);
                return new AssignmentNode(variable, expression, true);
            }
            else if(eat(TokenType.Semicolon)){ //if no assignment: let <identifier> : <type>;
                return variable;
            }
        }

        else if(currentToken.type == TokenType.Identifier){
            String identifier = currentToken.value;
            eat(TokenType.Identifier);
            if(eat(TokenType.EqualSign)){ //assignment: <identifier> = <expression>;
                VariableNode variable = new VariableNode("auto", identifier);
                ASTNode expression = parseExpression();
                eatFatal(TokenType.Semicolon);
                return new AssignmentNode(variable, expression, false);
            }
        }

        throw new Exception("Unknown command: \"" + currentToken.value + "\"");
    }

    private ASTNode parseFactor() throws Exception{ //for identifiers or ()
        if(currentToken.type == TokenType.Number){
            NumberNode number = new NumberNode(Double.parseDouble(currentToken.value));
            eatFatal(TokenType.Number);
            return number;
        }
        else if(currentToken.type == TokenType.Identifier){
            String identifier = currentToken.value;
            String type = "auto"; //auto is the same for unknown
            VariableNode node = new VariableNode(type, identifier);
            eatFatal(TokenType.Identifier);
            return node;
        }

        throw new Exception("Unexpected token in factor: " + currentToken.value);
    }

    private ASTNode parseTerm() throws Exception{ //for * or /
        ASTNode left = parseFactor();

        while(currentToken != null && (currentToken.type == TokenType.Asterisk || currentToken.type == TokenType.Slash)){
            String operator = currentToken.value;
            eatFatal(currentToken.type);
            ASTNode right = parseFactor();
            left = new BinaryOperationNode(left, operator, right);
        }

        return left;
    }

    private ASTNode parseExpression() throws Exception{ //for + or -
        ASTNode left = parseTerm();

        while(currentToken != null && (currentToken.type == TokenType.Plus || currentToken.type == TokenType.Minus)){
            String operator = currentToken.value;
            eatFatal(currentToken.type);
            ASTNode right = parseTerm();
            left = new BinaryOperationNode(left, operator, right);
        }

        return left;
    }

    private boolean eat(TokenType type) throws Exception{
        if(currentToken.type == type){
            currentToken = lexer.next();
            return true;
        }
        return false;
    }

    private void eatFatal(TokenType expected) throws Exception{
        if(currentToken.type != expected){
            throw new Exception(String.format("Unexpected token(%s)! Expected: %s", currentToken.type.toString(), expected.toString()));
        }
        else{
            currentToken = lexer.next();
        }
    }

    private void fatalCheck(TokenType expected) throws Exception{
        if(currentToken.type != expected){
            throw new Exception(String.format("Unexpected token(%s)! Expected: %s", currentToken.type.toString(), expected.toString()));
        }
    }

}
