package com.insotheo.fles.lexer;

public class Token {
    public TokenType type;
    public String value;

    public Token(TokenType t, String val) {
        type = t;
        value = val;
    }

}
