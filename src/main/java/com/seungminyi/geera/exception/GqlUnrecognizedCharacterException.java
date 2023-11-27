package com.seungminyi.geera.exception;

public class GqlUnrecognizedCharacterException extends RuntimeException {

    public GqlUnrecognizedCharacterException(String expected, char encountered, int line, int col) {
        super("기대한 값과 다릅니다. 기대값 : " + expected + ", 입력값 : " + encountered + " 위치 " + line + ":" + col);
    }
}
