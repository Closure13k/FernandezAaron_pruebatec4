package com.closure13k.aaronfmpt4.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomRequestDTO(
        @Pattern(
                regexp = "^[A-Z]{2}\\d{4}$",
                message = "'code' must be 2 uppercase letters followed by 4 digits")
        String code,
        
        @Size(
                min = 3, max = 50,
                message = "'type' must be between 3 and 50 characters")
        String type,
        
        LocalDate availableFrom,
        
        LocalDate availableTo,
        
        @DecimalMin(
                value = "0.0",
                inclusive = false,
                message = "'price' must be greater than 0")
        BigDecimal price
) {
}
