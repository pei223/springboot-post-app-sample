package com.example.postapp.controllers.common;

public class ErrorResponse {
    public String errorMessage;
    public String errorCode;

    public ErrorResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
