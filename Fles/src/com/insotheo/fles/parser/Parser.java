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
        String currentTokenValue = currentToken.value; //because identifier is everything so for handling exception correctly

        if(eat(TokenType.LBrace)){ //block
            return parseBlock();
        }

        else if(eat(TokenType.Let)){ //variable definition
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

        else if(eat(TokenType.Fn)){ //function definition
            fatalCheck(TokenType.Identifier);
            String funcName = currentToken.value;
            eatFatal(TokenType.Identifier);

            eatFatal(TokenType.LParen); //parsing parameters
            List<ParameterNode> funcParameters = new ArrayList<ParameterNode>();
            while(currentToken != null && currentToken.type != TokenType.RParen){
                if(currentToken.type == TokenType.Identifier){ //parameter: <identifier> : <data_type>
                    String parameterIdentifier = currentToken.value;
                    eatFatal(TokenType.Identifier);

                    eatFatal(TokenType.Colon);
                    fatalCheck(TokenType.Identifier);
                    String parameterType = currentToken.value;
                    eatFatal(TokenType.Identifier);

                    funcParameters.add(new ParameterNode(parameterType, parameterIdentifier));
                    if(!eat(TokenType.Comma)){
                        break;
                    }
                }
            }
            eatFatal(TokenType.RParen);

            eatFatal(TokenType.Colon);
            fatalCheck(TokenType.Identifier); //return type
            String funcReturnType = currentToken.value;
            eatFatal(TokenType.Identifier);

            eatFatal(TokenType.LBrace);
            BlockNode funcBody = parseBlock();

            return new FunctionNode(funcName, funcReturnType, funcParameters, funcBody);
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

        ParserExceptions.throwUnknownCommandException(currentTokenValue);
        return null;
    }

    private BlockNode parseBlock() throws Exception {
        List<ASTNode> blockStatements = new ArrayList<ASTNode>();
        while (currentToken != null && currentToken.type != TokenType.RBrace) {
            blockStatements.add(parseStatement());
        }
        eatFatal(TokenType.RBrace);
        return new BlockNode(blockStatements);
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

        ParserExceptions.throwUnexpectedTokenInFactorException(currentToken);
        return null;
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
            if(currentToken.type == TokenType.Unknown) {
                ParserExceptions.throwUnexpectedTokenException(currentToken.value, expected);
            }
            ParserExceptions.throwUnexpectedTokenException(currentToken.type, expected);
        }
        else{
            currentToken = lexer.next();
        }
    }

    private void fatalCheck(TokenType expected) throws Exception{
        if(currentToken.type != expected){
            if(currentToken.type == TokenType.Unknown) {
                ParserExceptions.throwUnexpectedTokenException(currentToken.value, expected);
            }
            ParserExceptions.throwUnexpectedTokenException(currentToken.type, expected);
        }
    }

}
