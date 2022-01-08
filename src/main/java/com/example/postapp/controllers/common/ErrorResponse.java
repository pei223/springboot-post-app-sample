package com.example.postapp.controllers.common;

public class ErrorResponse {
    public final String errorMessage;
    public final String errorCode;

    public ErrorResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
