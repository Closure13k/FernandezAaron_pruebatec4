package com.closure13k.aaronfmpt4.dto.request;

import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomRequestDTO(
        @Size(groups = {OnCreate.class}, min = 11, max = 11, message = "'code' must be 11 characters if provided.")
        String code,
        @NotNull(groups = {OnCreate.class}, message = "'type' must be provided on creation.")
        @Size(groups = {OnCreate.class, OnUpdate.class}, min = 3, max = 50, message = "'type' must be between 3 and 50 characters.")
        String type,
        @NotNull(groups = {OnCreate.class}, message = "'availableFrom' must be provided on creation.")
        LocalDate availableFrom,
        @NotNull(groups = {OnCreate.class}, message = "'availableTo' must be provided on creation.")
        LocalDate availableTo,
        @NotNull(groups = {OnCreate.class}, message = "'price' must be provided on creation.")
        @DecimalMin(groups = {OnCreate.class, OnUpdate.class}, value = "0.0", inclusive = false, message = "'price' must be greater than 0")
        BigDecimal price
) {
}
