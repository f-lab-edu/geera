package com.seungminyi.geera.common.dto;

public class ErrorResponseMessage {
    private String error;

    public ErrorResponseMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
