package com.closure13k.aaronfmpt4.dto.request;

import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record RoomBookingRequestDTO(
        @NotNull(groups = {OnCreate.class}, message = "'dateFrom' is required on creation.")
        LocalDate dateFrom,
        @NotNull(groups = {OnCreate.class}, message = "'dateTo' is required on creation.")
        LocalDate dateTo,
        @NotNull(groups = {OnCreate.class}, message = "'roomCode' is required on creation.")
        String roomCode,
        @NotEmpty(groups = {OnCreate.class}, message = "'clients' is required on creation.")
        List<ClientRequestDTO> clients
) {
}
