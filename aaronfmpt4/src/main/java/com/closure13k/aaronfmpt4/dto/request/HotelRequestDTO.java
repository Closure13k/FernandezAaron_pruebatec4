package com.closure13k.aaronfmpt4.dto.request;

import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HotelRequestDTO(
        @Pattern(groups = {OnCreate.class, OnUpdate.class},
                regexp = "^[A-Z]{4}\\d{4}$",
                message = "'code', if provided, must have 4 uppercase letters followed by 4 digits.")
        String code,
        
        @NotNull(groups = {OnCreate.class}, message = "The name must not be null on create.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 50, message = "'name' must have between 3 and 50 characters.")
        String name,
        
        @NotNull(groups = {OnCreate.class}, message = "'city' must not be null on create.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 50, message = "'city' must have between 3 and 50 characters.")
        String city
) {
}
