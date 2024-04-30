package com.closure13k.aaronfmpt4.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoomBookingResponseDTO(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        Integer nights,
        List<ClientResponseDTO> clients,
        RoomResponseDTO room,
        BigDecimal bookingCost
) {
}
