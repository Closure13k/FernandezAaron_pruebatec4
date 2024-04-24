package com.closure13k.aaronfmpt4.exception;

import com.closure13k.aaronfmpt4.model.Hotel;

public class HotelNotFoundException extends RuntimeException {
    public static HotelNotFoundException listIsEmpty() {
        return new HotelNotFoundException("No hotels found.");
    }
    
    public static HotelNotFoundException byId(Long id) {
        return new HotelNotFoundException("Hotel with id %d not found.".formatted(id));
    }
    
    public HotelNotFoundException() {
    }
    
    public HotelNotFoundException(String message) {
        super(message);
    }
    
    public HotelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
