package com.closure13k.aaronfmpt4.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClientResponseDTO(
        Long id,
        String nif,
        String name,
        String surname,
        String email
) {

}
