package com.closure13k.aaronfmpt4.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FlightBookingResponseDTO(
        Long id,
        String code,
        String seatType,
        ClientResponseDTO client,
        FlightResponseDTO flight
) {
}
