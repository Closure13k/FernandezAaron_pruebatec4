package com.closure13k.aaronfmpt4.exception;

/**
 * Exception thrown when an entity is not found.
 */
public class EntityNotFoundException extends RuntimeException {
    /**
     * Exception thrown when a list of entities is empty.
     * @param entity The entity name.
     */
    public static EntityNotFoundException listIsEmpty(String entity) {
        return new EntityNotFoundException("No list of %s found.".formatted(entity));
    }
    
    /**
     * Exception thrown when an entity with a specific id is not found.
     * @param entity The entity name.
     * @param id The entity id.
     */
    public static EntityNotFoundException byId(String entity, Long id) {
        return new EntityNotFoundException("%s with id %d not found.".formatted(entity, id));
    }
    
    /**
     * Exception thrown when an entity with a specific unique field is not found.
     * @param entity The entity name.
     * @param field The unique field name.
     * @param value The unique field value.
     */
    public static EntityNotFoundException byUniqueField(String entity, String field, String value) {
        return new EntityNotFoundException("%s with %s %s not found.".formatted(entity, field, value));
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
