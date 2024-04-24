package com.closure13k.aaronfmpt4.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FlightRequestDTO(
        @Size(
                min = 3, max = 20,
                message = "'code' must be between 3 and 20 characters")
        String code,
        
        @Size(
                min = 3, max = 100,
                message = "'origin' must be between 3 and 100 characters")
        String origin,
        
        @Size(
                min = 3, max = 100,
                message = "'destination' must be between 3 and 100 characters")
        String destination,
        
        LocalDate departureDate,
        
        @Size(
                min = 3, max = 20,
                message = "'seatType' must be between 3 and 20 characters")
        String seatType,
        
        @DecimalMin(
                value = "0",
                message = "'availableSeats' must be greater than 0")
        Integer availableSeats,
        
        @DecimalMin(
                value = "0.0",
                inclusive = false,
                message = "'price' must be greater than 0")
        BigDecimal price
) {
}
