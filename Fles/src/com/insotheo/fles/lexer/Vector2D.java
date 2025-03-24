package com.insotheo.fles.lexer;

public class Vector2D {
    private final int x, y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%d;%d", x, y);
    }
}
