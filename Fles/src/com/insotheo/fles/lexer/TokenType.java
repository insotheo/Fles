package com.insotheo.fles.lexer;

public enum TokenType {
    Plus, Minus, Asterisk, Slash, EqualSign, PercentSign, Point, At,
    Semicolon, Comma, Quotes, SingleQuote, Ampersand, Pipe,

    LessThan, MoreThan, LessOrEqual, MoreOrEqual, LogicOr, LogicAnd, Equality, LogicNot,
    LeftShift, RightShift,

    True, False,

    LParen, RParen, LBrace, RBrace, LSquareBracket, RSquareBracket,

    Number, StringLiteral, CharLiteral, Identifier,

    ArrayDef,

    EOF, Unknown
}
