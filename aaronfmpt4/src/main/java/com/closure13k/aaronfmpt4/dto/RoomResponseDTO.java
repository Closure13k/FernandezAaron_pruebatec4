package com.closure13k.aaronfmpt4.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoomResponseDTO(
        Long id,
        String code,
        String type,
        String price,
        String availableFrom,
        String availableTo,
        HotelResponseDTO hotel,
        Boolean isRemoved,
        List<RoomBookingResponseDTO> bookingList
) {
}
