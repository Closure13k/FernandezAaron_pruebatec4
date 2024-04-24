package com.closure13k.aaronfmpt4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FlightResponseDTO(
        Long id,
        String code,
        String origin,
        String destination,
        String departureDate,
        String seatType,
        Integer availableSeats,
        String price,
        Boolean isRemoved,
        List<FlightBookingResponseDTO> bookings
) {
}
