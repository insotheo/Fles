package com.insotheo.fles.lexer;

public enum TokenType {
    Plus, Minus, Asterisk, Slash, EqualSign, PercentSign, Dot, At, Colon,
    Semicolon, Comma, SingleQuote, Ampersand, Pipe, Bucks,

    LessThan, MoreThan, LessOrEqual, MoreOrEqual, LogicOr, LogicAnd, Equality, LogicNot, NotEqual,
    LeftShift, RightShift,

    If, Else,
    True, False,

    LParen, RParen, LBrace, RBrace, LSquareBracket, RSquareBracket,

    Number, StringLiteral, CharLiteral, Identifier,

    Module,
    ArrayDef, Let, Fn, Return, Delete,
    While,

    EOF, Unknown
}
