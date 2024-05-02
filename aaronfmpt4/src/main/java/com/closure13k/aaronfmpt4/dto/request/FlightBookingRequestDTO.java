package com.closure13k.aaronfmpt4.dto.request;

import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import jakarta.validation.constraints.NotNull;

public record FlightBookingRequestDTO(
        String code,
        @NotNull(groups = {OnCreate.class}, message = "'flightCode' is required for creation.")
        String flightCode,
        @NotNull(groups = {OnCreate.class}, message = "'client' is required for creation.")
        ClientRequestDTO client,
        @NotNull(groups = {OnCreate.class}, message = "'seatType' is required for creation.")
        String seatType
) {
}