package com.closure13k.aaronfmpt4.exception;

public class EntityNotFoundException extends RuntimeException {
    public static EntityNotFoundException listIsEmpty(String entity) {
        return new EntityNotFoundException("No list of %s found.".formatted(entity));
    }
    
    public static EntityNotFoundException byId(String entity, Long id) {
        return new EntityNotFoundException("%s with id %d not found.".formatted(entity, id));
    }
    
    public EntityNotFoundException() {
    }
    
    public EntityNotFoundException(String message) {
        super(message);
    }
    
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}