package com.closure13k.aaronfmpt4.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HotelRequestDTO(
        @Pattern(regexp = "^[A-Z]{2}\\d{4}$",
                message = "The code must have 2 uppercase letters followed by 4 digits.")
        String code,
        
        @Size(min = 3, max = 50,
                message = "The name must have between 3 and 50 characters.")
        String name,
        
        @Size(
                min = 3, max = 50,
                message = "The city must have between 3 and 50 characters."
        )
        String city
) {
}
