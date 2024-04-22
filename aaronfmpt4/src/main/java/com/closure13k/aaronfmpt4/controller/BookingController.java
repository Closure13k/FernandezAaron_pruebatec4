package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.RoomBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomBookingResponseDTO;
import com.closure13k.aaronfmpt4.service.IBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class BookingController {
    private final IBookingService service;
    
    @PostMapping("/room-booking/new")
    public ResponseEntity<RoomBookingResponseDTO> createRoomBooking(
            //? TODO: Add @Valid annotation after implementing the validation.
            @RequestBody RoomBookingRequestDTO roomBookingDTO) {
        return ResponseEntity.ok(service.createRoomBooking(roomBookingDTO));
    }
    
}
