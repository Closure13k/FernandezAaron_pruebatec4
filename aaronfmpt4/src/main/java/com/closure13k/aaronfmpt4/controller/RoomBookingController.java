package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.request.RoomBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.RoomBookingResponseDTO;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import com.closure13k.aaronfmpt4.service.IRoomBookingService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class RoomBookingController {
    private final IRoomBookingService service;
    
    @GetMapping("/room-booking")
    public ResponseEntity<List<RoomBookingResponseDTO>> getRoomBookings() {
        return ResponseEntity.ok(service.getAllRoomBookings());
    }
    
    @GetMapping("/room-booking/{id}")
    public ResponseEntity<RoomBookingResponseDTO> getRoomBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRoomBookingById(id));
    }
    
    @PostMapping("/room-booking/new")
    public ResponseEntity<RoomBookingResponseDTO> createRoomBooking(@Validated(OnCreate.class)
                                                                    @RequestBody RoomBookingRequestDTO booking) {
        return ResponseEntity.ok(service.createRoomBooking(booking));
    }
    
    
    @PutMapping("/room-booking/edit/{id}")
    public ResponseEntity<Void> updateRoomBooking(
            @Positive @PathVariable Long id,
            @Validated(OnUpdate.class)
            @RequestBody RoomBookingRequestDTO roomBookingDTO) {
        service.updateRoomBooking(id, roomBookingDTO);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/room-booking/delete/{id}")
    public ResponseEntity<Void> deleteRoomBooking(@PathVariable Long id) {
        service.deleteRoomBooking(id);
        return ResponseEntity.accepted().build();
    }
}
