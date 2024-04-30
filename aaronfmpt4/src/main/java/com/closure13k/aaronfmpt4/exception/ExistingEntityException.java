package com.closure13k.aaronfmpt4.exception;

/**
 * An exception that indicates that an entity already exists.
 * <p>
 * This exception should be thrown when an entity is being created or updated and the entity already exists, be it by id
 * or by a unique field.
 * <p>
 * This exception should also be thrown when an entity is being deleted and it has active children.
 */
public class ExistingEntityException extends RuntimeException {
    /**
     * Returns an exception that indicates that the entity with the given id already exists.
     *
     * @param entityName The entity name.
     * @param id         The id.
     * @return The exception.
     */
    public static ExistingEntityException byId(String entityName, Long id) {
        return new ExistingEntityException("Id in " + entityName + " already exists: " + id);
    }
    
    /**
     * Returns an exception that indicates that the entity with the given unique field value already exists.
     *
     * @param entityName The entity name.
     * @param fieldName  The unique field name.
     * @param fieldValue The unique field value.
     * @return The exception.
     */
    public static ExistingEntityException byUniqueField(String entityName, String fieldName, String fieldValue) {
        return new ExistingEntityException(
                "Unique value in %s already exists: %s = %s"
                        .formatted(entityName, fieldName, fieldValue)
        );
    }
    
    /**
     * Returns an exception that indicates that the entity with the given id has active children.
     *
     * @param parent The parent entity name.
     * @param child  The child entity name.
     * @param id     The id of the parent entity.
     * @return The exception.
     */
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
