package com.insotheo.fles.token;

public enum TokenType {
    Plus, Minus, Asterisk, Slash, EqualSign, PercentSign, Point,
    Semicolon, Comma, Quotes, SingleQuote, Ampersand, Pipe,

    LessThan, MoreThan, LessOrEqual, MoreOrEqual, LogicOr, LogicAnd, Equality, LogicNot,
    LeftShift, RightShift,

    LParen, RParen, LBrace, RBrace, LSquareBracket, RSquareBracket,

    Number, StringLiteral, CharLiteral, Identifier,

    EOF, Unknown
}
