package com.insotheo.fles.parser;

import com.insotheo.fles.lexer.Token;
import com.insotheo.fles.lexer.TokenType;
import com.insotheo.fles.lexer.TokenTypeDecoder;

public class ParserExceptions {
    public static void throwUnexpectedTokenException(TokenType current, TokenType expected) throws Exception {
        throw new Exception(String.format("Unexpected token(%s)! Expected: %s", TokenTypeDecoder.tokenTypeToString(current), TokenTypeDecoder.tokenTypeToString(expected)));
    }

    public static void throwUnexpectedTokenException(String current, TokenType expected) throws Exception {
        throw new Exception(String.format("Unexpected token(%s)! Expected: %s", current, TokenTypeDecoder.tokenTypeToString(expected)));
    }

    public static void throwUnexpectedTokenInFactorException(Token token) throws Exception {
        throw new Exception(String.format("Unexpected token in factor: %s", token.value));
    }

    public static void throwUnknownCommandException(String command) throws Exception {
        throw new Exception(String.format("Unknown command \"%s\"", command));
    }
}
