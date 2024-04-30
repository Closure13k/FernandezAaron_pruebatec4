package com.closure13k.aaronfmpt4.dto.request;

import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomRequestDTO(
        @Length(min = 11, max = 11, message = "'code' must be 11 characters if provided.")
        String code,
        @NotNull(groups = {OnCreate.class}, message = "'type' must be provided on creation.")
        @Size(min = 3, max = 50, message = "'type' must be between 3 and 50 characters.")
        String type,
        
        @NotNull(groups = {OnCreate.class}, message = "'availableFrom' must be provided on creation.")
        LocalDate availableFrom,
        @NotNull(groups = {OnCreate.class}, message = "'availableTo' must be provided on creation.")
        LocalDate availableTo,
        
        @NotNull(groups = {OnCreate.class}, message = "'price' must be provided on creation.")
        @DecimalMin(value = "0.0", inclusive = false, message = "'price' must be greater than 0")
        BigDecimal price
) {
}
