package com.closure13k.aaronfmpt4.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FlightResponseDTO(
        Long id,
        String code,
        String origin,
        String destination,
        String departureDate,
        Integer availableSeats,
        BigDecimal price,
        Boolean isRemoved,
        List<FlightBookingResponseDTO> bookings
) {
}
