package com.seungminyi.geera.exception;

public class UnrecognizedCharacterException extends RuntimeException {
    public UnrecognizedCharacterException(String message, int line, int col) {
        super(message);
    }

    public UnrecognizedCharacterException(String expected, char encountered, int line, int col) {
        super("Expected: " + expected + ", but encountered: " + encountered + " at " + line + ":" + col);
    }
}
