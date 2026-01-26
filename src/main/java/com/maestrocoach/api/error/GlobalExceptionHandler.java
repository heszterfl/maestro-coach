package com.maestrocoach.api.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(IllegalArgumentException ex, HttpServletRequest req) {
        ApiErrorResponse body = new ApiErrorResponse("Resource not found", HttpStatus.NOT_FOUND.value(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(ResponseStatusException ex,
                                                                          HttpServletRequest req) {
        ApiErrorResponse body = new ApiErrorResponse(ex.getReason() != null ? ex.getReason() : "Resource not found", ex.getStatusCode().value(), req.getRequestURI());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidMethodArgument(MethodArgumentNotValidException ex,
                                                                        HttpServletRequest req) {
        ApiErrorResponse body = new ApiErrorResponse("Validation failed", HttpStatus.BAD_REQUEST.value(), req.getRequestURI());
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }
}
