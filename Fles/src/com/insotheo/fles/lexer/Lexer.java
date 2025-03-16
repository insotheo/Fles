package com.insotheo.fles.lexer;

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

    public Token next() throws Exception{
        while(pos < content.length()){
            if(Character.isWhitespace(content.charAt(pos)) || content.charAt(pos) == ' '){
                pos++;
                continue;
            }
            char current = content.charAt(pos);

            if(Character.isDigit(current)){
                String number = "";
                boolean floatPoint = false;
                while(pos < content.length() && (Character.isDigit(content.charAt(pos)) || content.charAt(pos) == '.')){
                    if(content.charAt(pos) == '.'){
                        if(floatPoint){
                            throw new Exception(String.format("Incorrect number format at index %d!", pos));
                        }
                        floatPoint = true;
                    }
                    number += content.charAt(pos);
                    pos++;
                }
                if(number.endsWith(".")){
                    number += "0";
                }
                return new Token(TokenType.Number, number);
            }

            if(Character.isLetter(current) || current == '_'){
                String identifier = "";
                while(pos < content.length() &&
                        (Character.isLetter(content.charAt(pos)) ||
                                Character.isDigit(content.charAt(pos)) ||
                                content.charAt(pos) == '_')){
                    identifier += content.charAt(pos);
                    pos++;
                }

                switch (identifier){
                    case "true": return new Token(TokenType.True, "true");
                    case "false": return new Token(TokenType.False, "false");
                    case "let": return new Token(TokenType.Let, "let");
                    case "fn": return new Token(TokenType.Fn, "fn");
                    case "return": return new Token(TokenType.Return, "return");
                }

                return new Token(TokenType.Identifier, identifier);
            }

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
                case '%': pos++; return new Token(TokenType.PercentSign, "%");

                case '(': pos++; return new Token(TokenType.LParen, "(");
                case ')': pos++; return new Token(TokenType.RParen, ")");
                case '{': pos++; return new Token(TokenType.LBrace, "{");
                case '}': pos++; return new Token(TokenType.RBrace, "}");
                case '[':{
                    pos++;
                    if(content.charAt(pos) == ']'){
                        pos++;
                        return new Token(TokenType.ArrayDef, "[]");
                    }
                    return new Token(TokenType.LSquareBracket, "[");
                }
                case ']': pos++; return new Token(TokenType.RSquareBracket, "]");

                case '.': pos++; return new Token(TokenType.Point, ".");
                case ';': pos++; return new Token(TokenType.Semicolon, ";");
                case ',': pos++; return new Token(TokenType.Comma, ",");
                case '@': pos++; return new Token(TokenType.At, "@");
                case ':': pos++; return new Token(TokenType.Colon, ":");

                case '=':{
                    pos++;
                    if(content.charAt(pos) == '='){
                        pos++;
                        return new Token(TokenType.Equality, "==");
                    }
                    return new Token(TokenType.EqualSign, "=");
                }
                case '&':{
                    pos++;
                    if(content.charAt(pos) == '&'){
                        pos++;
                        return new Token(TokenType.LogicAnd, "&&");
                    }
                    return new Token(TokenType.Ampersand, "&");
                }
                case '|':{
                    pos++;
                    if(content.charAt(pos) == '|'){
                        pos++;
                        return new Token(TokenType.LogicOr, "||");
                    }
                    return new Token(TokenType.Pipe, "|");
                }
                case '!': pos++; return new Token(TokenType.LogicNot, "!");
                case '>':{
                    pos++;
                    if(content.charAt(pos) == '='){
                        pos++;
                        return new Token(TokenType.MoreOrEqual, ">=");
                    }
                    else if(content.charAt(pos) == '>'){
                        pos++;
                        return new Token(TokenType.RightShift, ">>");
                    }
                    return new Token(TokenType.MoreThan, ">");
                }
                case '<': {
                    pos++;
                    if(content.charAt(pos) == '='){
                        pos++;
                        return new Token(TokenType.LessOrEqual, "<=");
                    }
                    else if(content.charAt(pos) == '<'){
                        pos++;
                        return new Token(TokenType.LeftShift, "<<");
                    }
                    return new Token(TokenType.LessThan, "<");
                }

                case '\'':{
                    pos++;
                    if(content.charAt(pos) != '\''){
                        String literal = String.valueOf(content.charAt(pos));
                        pos++;
                        if(pos < content.length() && content.charAt(pos) == '\''){
                            pos++;
                        }
                        return new Token(TokenType.CharLiteral, literal);
                    }
                    return new Token(TokenType.SingleQuote, "'");
                }
                case '"':{
                    pos++;
                    if(content.charAt(pos) != '"'){
                        String literal = "";
                        while(pos < content.length() && content.charAt(pos) != '"'){
                            literal += content.charAt(pos);
                            pos++;
                        }
                        if(pos < content.length() && content.charAt(pos) == '"'){
                            pos++;
                        }
                        return new Token(TokenType.StringLiteral, literal);
                    }
                    return new Token(TokenType.Quotes, "\"");
                }
            }

            pos++;
            return new Token(TokenType.Unknown, String.format("Unknown: '%c'", current));
        }
        return new Token(TokenType.EOF, "EOF");
    }

    public Vector2D getPosition(){
        int line = 1;
        int column = 1;

        for(int i = 0; i < pos; i++){
            if(content.charAt(i) == '\n'){
                line++;
                column = 1;
            }
            else{
                column++;
            }
        }

        return new Vector2D(line, column);
    }

}
