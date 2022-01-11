package com.example.postapp.services.common;

public class PostAppException extends Exception {
    public final String errorCode;

    public PostAppException(String errorMessage, String errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
