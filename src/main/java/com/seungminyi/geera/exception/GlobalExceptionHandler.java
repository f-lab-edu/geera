package com.seungminyi.geera.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InsufficientPermissionException.class)
    public ResponseEntity<String> handleProjectPermissionException(InsufficientPermissionException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedAssignmentException.class)
    public ResponseEntity<String> handleUnauthorizedAssignmentException(UnauthorizedAssignmentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(MaxItemsExceededException.class)
    public ResponseEntity<String> handleMaxItemsExceededException(UnauthorizedAssignmentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
