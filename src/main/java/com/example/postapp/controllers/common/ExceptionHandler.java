package com.example.postapp.controllers.common;

import com.example.postapp.services.common.AlreadyExistsException;
import com.example.postapp.services.common.NotAuthorizedException;
import com.example.postapp.services.common.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<?> handleAllException(Exception ex, WebRequest request) throws Exception {
        if (ex instanceof NotFoundException) {
            return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage(), ((NotFoundException) ex).errorCode));
        }
        if (ex instanceof NotAuthorizedException) {
            return ResponseEntity.status(401).body(new ErrorResponse(ex.getMessage(), ((NotAuthorizedException) ex).errorCode));
        }
        if (ex instanceof AlreadyExistsException) {
            return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage(), ((AlreadyExistsException) ex).errorCode));
        }
        return super.handleException(ex, request);
    }
}
