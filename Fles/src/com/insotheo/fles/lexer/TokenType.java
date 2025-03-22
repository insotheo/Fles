package com.insotheo.fles.lexer;

public enum TokenType {
    Plus, Minus, Asterisk, Slash, EqualSign, PercentSign, Point, At, Colon,
    Semicolon, Comma, SingleQuote, Ampersand, Pipe,

    LessThan, MoreThan, LessOrEqual, MoreOrEqual, LogicOr, LogicAnd, Equality, LogicNot,
    LeftShift, RightShift,

    True, False,

    LParen, RParen, LBrace, RBrace, LSquareBracket, RSquareBracket,

    Number, StringLiteral, CharLiteral, Identifier,

    ArrayDef, Let, Fn, Return, Delete, GlobalModifier,

    EOF, Unknown
}
