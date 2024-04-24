package com.closure13k.aaronfmpt4.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomRequestDTO(
        @Pattern(
                regexp = "^[A-Z]{2}\\d{4}$",
                message = "Code must be 2 uppercase letters followed by 4 digits"
        )
        String code,
        @Size(min = 3, max = 50)
        String type,
        LocalDate availableFrom,
        LocalDate availableTo,
        
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal price
) {
}
