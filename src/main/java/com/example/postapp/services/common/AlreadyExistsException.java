package com.example.postapp.services.common;

public class AlreadyExistsException extends PostAppException {
    public AlreadyExistsException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
