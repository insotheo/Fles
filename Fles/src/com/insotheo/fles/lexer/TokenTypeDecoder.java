package com.insotheo.fles.lexer;

public class TokenTypeDecoder {
    public static String tokenTypeToString(TokenType type){
        return switch (type){
            case TokenType.PercentSign -> "'%'";
            case TokenType.EqualSign -> "'='";
            case TokenType.At -> "'@'";
            case TokenType.LBrace -> "'{'";
            case TokenType.RBrace -> "'}'";
            case TokenType.LSquareBracket -> "'['";
            case TokenType.RSquareBracket -> "']'";
            case TokenType.LParen -> "'('";
            case TokenType.RParen -> "')'";
            case TokenType.CharLiteral -> "Char literal";
            case TokenType.StringLiteral -> "String literal";
            case TokenType.LeftShift -> "Left shift";
            case TokenType.RightShift -> "Right shift";
            default -> type.toString();
        };
    }
}
