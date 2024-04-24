package com.closure13k.aaronfmpt4.exception;

public class RoomNotFoundException extends RuntimeException {
    public static RoomNotFoundException listIsEmpty() {
        return new RoomNotFoundException("No rooms found.");
    }
    
    public static RoomNotFoundException byId(Long id) {
        return new RoomNotFoundException("Room with id %d not found.".formatted(id));
    }
    
    public RoomNotFoundException() {
    }
    
    public RoomNotFoundException(String message) {
        super(message);
    }
    
    public RoomNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
