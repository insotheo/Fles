package com.insotheo.fles.lexer;

public enum TokenType {
    Plus, Minus, Asterisk, Slash, EqualSign, PercentSign, Point, At, Colon,
    Semicolon, Comma, SingleQuote, Ampersand, Pipe, Bucks,

    LessThan, MoreThan, LessOrEqual, MoreOrEqual, LogicOr, LogicAnd, Equality, LogicNot, NotEqual,
    LeftShift, RightShift,

    If, Else,
    True, False,

    LParen, RParen, LBrace, RBrace, LSquareBracket, RSquareBracket,

    Number, StringLiteral, CharLiteral, Identifier,

    ArrayDef, Let, Fn, Return, Delete, GlobalModifier,

    EOF, Unknown
}
