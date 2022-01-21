package com.example.postapp.controllers.common;

import java.util.Map;

public class ErrorResponse {
    public final String errorMessage;
    public final String errorCode;
    public final Map<String, String> data;

    public ErrorResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.data = Map.of();
    }

    public ErrorResponse(String errorMessage, String errorCode, Map<String, String> data) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.data = data;
    }

    public ErrorResponse() {
        this.errorMessage = "";
        this.errorCode = "";
        this.data = Map.of();
    }
}
