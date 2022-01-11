package com.example.postapp.services.common;

public class NotFoundException extends PostAppException {
    public NotFoundException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
