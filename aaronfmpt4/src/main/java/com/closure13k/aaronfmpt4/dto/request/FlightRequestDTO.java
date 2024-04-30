package com.closure13k.aaronfmpt4.dto.request;

import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FlightRequestDTO(
        @Size(min = 3, max = 20, message = "'code' must be between 3 and 20 characters")
        String code,
        
        @NotNull(groups = {OnCreate.class}, message = "'origin' must not be null on create.")
        @Size(min = 3, max = 100, message = "'origin' must be between 3 and 100 characters")
        String origin,
        
        @NotNull(groups = {OnCreate.class}, message = "'destination' must not be null on create.")
        @Size(min = 3, max = 100, message = "'destination' must be between 3 and 100 characters")
        String destination,
        
        @NotNull(groups = {OnCreate.class}, message = "'departureDate' must not be null on create.")
        LocalDate departureDate,
        
        @NotNull(groups = {OnCreate.class}, message = "'availableSeats' must not be null on create.")
        @DecimalMin(value = "0", message = "'availableSeats' must be greater than 0")
        Integer availableSeats,
        
        @NotNull(groups = {OnCreate.class}, message = "'price' must not be null on create.")
        @DecimalMin(value = "0.0", inclusive = false, message = "'price' must be greater than 0")
        BigDecimal price
) {
}
