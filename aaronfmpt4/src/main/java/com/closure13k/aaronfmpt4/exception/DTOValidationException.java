package com.closure13k.aaronfmpt4.exception;

public class DTOValidationException extends RuntimeException {
    public DTOValidationException(String message) {
        super(message);
    }
    
    /**
     * Exception for when there are no fields to update from the request.
     */
    public static DTOValidationException noFieldsToUpdate() {
        return new DTOValidationException("There are no fields to update in the request.");
    }
}