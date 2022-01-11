package com.example.postapp.services.common;

public class NotAuthorizedException extends PostAppException {
    public NotAuthorizedException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);
    }
}
