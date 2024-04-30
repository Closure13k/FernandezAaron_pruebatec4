package com.closure13k.aaronfmpt4.dto.request;

import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record ClientRequestDTO(
        @NotNull(groups = {OnCreate.class}, message = "NIF is required for creation.")
        @Null(groups = {OnUpdate.class}, message = "NIF must be null on update.")
        String nif,
        @NotNull(groups = {OnCreate.class}, message = "Name is required for creation.")
        String name,
        @NotNull(groups = {OnCreate.class}, message = "Surname is required for creation.")
        String surname,
        @NotNull(groups = {OnCreate.class}, message = "Email is required for creation.")
        String email
) {
}
