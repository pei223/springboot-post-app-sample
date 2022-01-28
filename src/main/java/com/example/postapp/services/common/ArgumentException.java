package com.example.postapp.services.common;

public class ArgumentException extends PostAppException {
    public ArgumentException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}

