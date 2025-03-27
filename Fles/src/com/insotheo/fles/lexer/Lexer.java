package com.insotheo.fles.lexer;

public class Lexer {
    private String content;
    private int pos;

    public Lexer(String input) throws Exception {
        content = input;
        pos = 0;
        if (content.isEmpty()) {
            throw new Exception("Content length is zero!");
        }
        content += " ";
    }

    public Token next() throws Exception {
        while (pos < content.length()) {
            if (Character.isWhitespace(content.charAt(pos)) || content.charAt(pos) == ' ') {
                pos++;
                continue;
            }
            char current = content.charAt(pos);

            if (Character.isDigit(current)) {
                String number = "";
                boolean floatPoint = false;
                while (pos < content.length() && (Character.isDigit(content.charAt(pos)) || content.charAt(pos) == '.')) {
                    if (content.charAt(pos) == '.') {
                        if (floatPoint) {
                            throw new Exception(String.format("Incorrect number format at index %d!", pos));
                        }
                        floatPoint = true;
                    }
                    number += content.charAt(pos);
                    pos++;
                }
                if (number.endsWith(".")) {
                    number += "0";
                }
                return new Token(TokenType.Number, number);
            }

            if (Character.isLetter(current) || current == '_') {
                String identifier = "";
                while (pos < content.length() &&
                        (Character.isLetter(content.charAt(pos)) ||
                                Character.isDigit(content.charAt(pos)) ||
                                content.charAt(pos) == '_')) {
                    identifier += content.charAt(pos);
                    pos++;
                }

                switch (identifier) {
                    case "module":
                        return new Token(TokenType.Module, null);
                    case "true":
                        return new Token(TokenType.True, null);
                    case "false":
                        return new Token(TokenType.False, null);
                    case "let":
                        return new Token(TokenType.Let, null);
                    case "fn":
                        return new Token(TokenType.Fn, null);
                    case "return":
                        return new Token(TokenType.Return, null);
                    case "delete":
                        return new Token(TokenType.Delete, null);
                    case "if":
                        return new Token(TokenType.If, null);
                    case "else":
                        return new Token(TokenType.Else, null);
                    case "while":
                        return new Token(TokenType.While, null);
                }

                return new Token(TokenType.Identifier, identifier);
            }

            switch (current) {
                case '+':
                    pos++;
                    return new Token(TokenType.Plus, null);
                case '-':
                    pos++;
                    return new Token(TokenType.Minus, null);
                case '*':
                    pos++;
                    return new Token(TokenType.Asterisk, null);
                case '/': {
                    pos++;
                    if (content.charAt(pos) == '/') { //comment
                        while (pos < content.length() && content.charAt(pos) != '\n') {
                            pos++;
                        }
                        continue;
                    } else { //Slash
                        return new Token(TokenType.Slash, null);
                    }
                }
                case '%':
                    pos++;
                    return new Token(TokenType.PercentSign, null);

                case '(':
                    pos++;
                    return new Token(TokenType.LParen, null);
                case ')':
                    pos++;
                    return new Token(TokenType.RParen, null);
                case '{':
                    pos++;
                    return new Token(TokenType.LBrace, null);
                case '}':
                    pos++;
                    return new Token(TokenType.RBrace, null);
                case '[': {
                    pos++;
                    if (content.charAt(pos) == ']') {
                        pos++;
                        return new Token(TokenType.ArrayDef, null); //[]
                    }
                    return new Token(TokenType.LSquareBracket, null);
                }
                case ']':
                    pos++;
                    return new Token(TokenType.RSquareBracket, null);

                case '.':
                    pos++;
                    return new Token(TokenType.Dot, null);
                case ';':
                    pos++;
                    return new Token(TokenType.Semicolon, null);
                case ',':
                    pos++;
                    return new Token(TokenType.Comma, null);
                case '@':
                    pos++;
                    return new Token(TokenType.At, null);
                case ':':
                    pos++;
                    return new Token(TokenType.Colon, null);
                case '$':
                    pos++;
                    return new Token(TokenType.Bucks, null);

                case '=': {
                    pos++;
                    if (content.charAt(pos) == '=') {
                        pos++;
                        return new Token(TokenType.Equality, null); //==
                    }
                    return new Token(TokenType.EqualSign, null);
                }
                case '&': {
                    pos++;
                    if (content.charAt(pos) == '&') {
                        pos++;
                        return new Token(TokenType.LogicAnd, null); //&&
                    }
                    return new Token(TokenType.Ampersand, null);
                }
                case '|': {
                    pos++;
                    if (content.charAt(pos) == '|') {
                        pos++;
                        return new Token(TokenType.LogicOr, null); //||
                    }
                    return new Token(TokenType.Pipe, null);
                }
                case '!': {
                    pos++;
                    if (content.charAt(pos) == '=') {
                        pos++;
                        return new Token(TokenType.NotEqual, null);
                    }
                    return new Token(TokenType.LogicNot, null);
                } //!

                case '>': {
                    pos++;
                    if (content.charAt(pos) == '=') {
                        pos++;
                        return new Token(TokenType.MoreOrEqual, null); //>=
                    } else if (content.charAt(pos) == '>') {
                        pos++;
                        return new Token(TokenType.RightShift, null); //>>
                    }
                    return new Token(TokenType.MoreThan, null); //>
                }
                case '<': {
                    pos++;
                    if (content.charAt(pos) == '=') {
                        pos++;
                        return new Token(TokenType.LessOrEqual, null); //<=
                    } else if (content.charAt(pos) == '<') {
                        pos++;
                        return new Token(TokenType.LeftShift, null); //<<
                    }
                    return new Token(TokenType.LessThan, null); //<
                }

                case '\'': {
                    pos++;
                    if (content.charAt(pos) != '\'') {
                        String literal = String.valueOf(content.charAt(pos));
                        pos++;
                        if (pos < content.length() && content.charAt(pos) == '\'') {
                            pos++;
                        }
                        return new Token(TokenType.CharLiteral, literal);
                    }
                    return new Token(TokenType.SingleQuote, null);
                }
                case '"': {
                    pos++;
                    if (content.charAt(pos) != '"') {
                        String literal = "";
                        while (pos < content.length() && content.charAt(pos) != '"') {
                            literal += content.charAt(pos);
                            pos++;
                        }
                        if (pos < content.length() && content.charAt(pos) == '"') {
                            pos++;
                        }
                        return new Token(TokenType.StringLiteral, literal);
                    }
                    pos++;
                    return new Token(TokenType.StringLiteral, "");
                }
            }

            pos++;
            return new Token(TokenType.Unknown, String.format("Unknown: '%c'", current));
        }
        return new Token(TokenType.EOF, null);
    }

    public Vector2D getPosition() {
        int line = 1;
        int column = 1;

        for (int i = 0; i < pos; i++) {
            if (content.charAt(i) == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }

        return new Vector2D(line, column);
    }

}
