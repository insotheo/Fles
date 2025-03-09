package com.insotheo.fles.token;

public class Lexer {
    private String content;
    private int pos;

    public Lexer(String input) throws Exception {
        content = input;
        pos = 0;
        if(content.length() == 0){
            throw new Exception("Content length is zero!");
        }
    }

    public Token next(){
        while(pos < content.length()){
            if(Character.isWhitespace(content.charAt(pos))){
                pos++;
                continue;
            }

            return new Token(TokenType.Unknown, String.format("Unknown char: '%c'", content.charAt(pos)));
        }
        return new Token(TokenType.Unknown, String.format("Unknown char: '%c'", content.charAt(pos)));
    }

}
