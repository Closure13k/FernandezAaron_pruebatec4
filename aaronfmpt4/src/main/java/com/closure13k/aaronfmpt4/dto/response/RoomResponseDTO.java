package com.closure13k.aaronfmpt4.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoomResponseDTO(
        Long id,
        String code,
        String type,
        BigDecimal price,
        String availableFrom,
        String availableTo,
        HotelResponseDTO hotel,
        Boolean isRemoved,
        List<RoomBookingResponseDTO> bookingList
) {
}
