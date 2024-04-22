package com.closure13k.aaronfmpt4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record HotelResponseDTO(
        Integer id,
        String code,
        String name,
        String city,
        Boolean isRemoved,
        List<RoomDTO> rooms
) {
}