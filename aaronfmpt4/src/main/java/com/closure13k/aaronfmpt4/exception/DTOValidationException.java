package com.closure13k.aaronfmpt4.exception;

public class DTOValidationException extends RuntimeException {
    public DTOValidationException(String message) {
        super(message);
    }
    
    public static DTOValidationException noFieldsToUpdate() {
        return new DTOValidationException("There are no fields to update in the request.");
    }
}