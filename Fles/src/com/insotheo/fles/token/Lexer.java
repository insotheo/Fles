package com.insotheo.fles.token;

public class Lexer {
    private String content;
    private int pos;

    public Lexer(String input) throws Exception {
        content = input;
        pos = 0;
        if(content.isEmpty()){
            throw new Exception("Content length is zero!");
        }
        content += " ";
    }

    public Token next(){
        while(pos < content.length()){
            if(Character.isWhitespace(content.charAt(pos)) || content.charAt(pos) == ' '){
                pos++;
                continue;
            }
            char current = content.charAt(pos);

            switch (current){
                case '+': pos++; return new Token(TokenType.Plus, "+");
                case '-': pos++; return new Token(TokenType.Minus, "-");
                case '*': pos++; return new Token(TokenType.Asterisk, "*");
                case '/': {
                    pos++;
                    if (content.charAt(pos) == '/') { //comment
                        while (pos < content.length() && content.charAt(pos) != '\n') {
                            pos++;
                        }
                        continue;
                    } else { //Slash
                        return new Token(TokenType.Slash, "/");
                    }
                }
                case '(': pos++; return new Token(TokenType.LParen, "(");
                case ')': pos++; return new Token(TokenType.RParen, ")");
            }

            pos++;
            return new Token(TokenType.Unknown, String.format("Unknown: '%c'", current));
        }
        return new Token(TokenType.EOF, "EOF");
    }

}
