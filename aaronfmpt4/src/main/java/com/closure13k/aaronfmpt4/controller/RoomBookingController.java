package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.request.RoomBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.RoomBookingResponseDTO;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import com.closure13k.aaronfmpt4.service.IRoomBookingService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    
    /**
     * Get all room bookings
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved room bookings"),
            @ApiResponse(responseCode = "404", description = "No room bookings found")
    })
    @GetMapping("/room-booking")
    public ResponseEntity<List<RoomBookingResponseDTO>> getRoomBookings() {
        return ResponseEntity.ok(service.getAllRoomBookings());
    }
    
    /**
     * Get a room booking by id
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a room booking"),
            @ApiResponse(responseCode = "404", description = "Room booking not found")
    })
    @GetMapping("/room-booking/{id}")
    public ResponseEntity<RoomBookingResponseDTO> getRoomBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getRoomBookingById(id));
    }
    
    /**
     * Create a new room booking
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a room booking"),
            @ApiResponse(responseCode = "400", description = "If the request body is invalid"),
    })
    @PostMapping("/room-booking/new")
    public ResponseEntity<RoomBookingResponseDTO> createRoomBooking(@Validated(OnCreate.class)
                                                                    @RequestBody RoomBookingRequestDTO booking) {
        return ResponseEntity.ok(service.createRoomBooking(booking));
    }
    
    /**
     * Update a room booking
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a room booking"),
            @ApiResponse(responseCode = "400", description = "If the request body is invalid"),
            @ApiResponse(responseCode = "404", description = "Room booking not found")
    })
    @PutMapping("/room-booking/edit/{id}")
    public ResponseEntity<Void> updateRoomBooking(
            @Positive @PathVariable Long id,
            @Validated(OnUpdate.class)
            @RequestBody RoomBookingRequestDTO roomBookingDTO) {
        service.updateRoomBooking(id, roomBookingDTO);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Delete a room booking
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully deleted a room booking"),
            @ApiResponse(responseCode = "404", description = "Room booking not found")
    })
    @DeleteMapping("/room-booking/delete/{id}")
    public ResponseEntity<Void> deleteRoomBooking(@PathVariable Long id) {
        service.deleteRoomBooking(id);
        return ResponseEntity.accepted().build();
    }
}
