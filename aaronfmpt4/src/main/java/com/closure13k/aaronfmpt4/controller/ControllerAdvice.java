package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.exception.DTOValidationException;
import com.closure13k.aaronfmpt4.exception.EntityNotFoundException;
import com.closure13k.aaronfmpt4.exception.ExistingEntityException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    
    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERRORS = "errors";
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST);
        body.put(ERRORS, ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList());
        
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundException(EntityNotFoundException e) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    @ExceptionHandler(ExistingEntityException.class)
    public ResponseEntity<Map<String, String>> handleExistingEntityException(ExistingEntityException e) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
    
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, List<String>>> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        Map<String, List<String>> response = new HashMap<>();
        List<String> errors = e.getAllErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();
        
        response.put(ERRORS, errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.METHOD_NOT_ALLOWED);
        body.put(MESSAGE, "Method " + ex.getMethod() + " is not supported for this request. Supported methods are " + ex.getSupportedHttpMethods());
        
        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    @ExceptionHandler(DTOValidationException.class)
    public ResponseEntity<Map<String, String>> handleDTOValidationException(DTOValidationException e) {
        Map<String, String> response = new HashMap<>();
        response.put(MESSAGE, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
}
