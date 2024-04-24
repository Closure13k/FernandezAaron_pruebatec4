package com.closure13k.aaronfmpt4.exception;

public class ExistingEntityException extends RuntimeException {
    public static ExistingEntityException byId(String entityName, Long id) {
        return new ExistingEntityException("Id in " + entityName + " already exists: " + id);
    }
    
    public static ExistingEntityException byUniqueField(String entityName, String fieldName, String fieldValue) {
        return new ExistingEntityException(
                "Unique value in %s already exists: %s = %s"
                        .formatted(entityName, fieldName, fieldValue)
        );
    }
    
    public static ExistingEntityException hasActiveChildren(String parent, String child, Long id) {
        return new ExistingEntityException(
                "Cannot delete %s (id: %d) with active %s.%nPlease delete the %s first."
                        .formatted(parent, id, child, child)
        );
    }
    
    
    public ExistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ExistingEntityException(String message) {
        super(message);
    }
    
    public ExistingEntityException() {
    }
    
    
}
