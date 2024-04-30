package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.request.FlightBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightBookingResponseDTO;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import com.closure13k.aaronfmpt4.service.IFlightBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class FlightBookingController {
    private final IFlightBookingService service;
    
    @GetMapping("/flight-booking")
    public ResponseEntity<List<FlightBookingResponseDTO>> getFlightBookings() {
        return ResponseEntity.ok(service.getAllFlightBookings());
    }
    
    @GetMapping("/flight-booking/{id}")
    public ResponseEntity<FlightBookingResponseDTO> getFlightBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFlightBookingById(id));
    }
    
    @PostMapping("/flight-booking/new")
    public ResponseEntity<FlightBookingResponseDTO> createFlightBooking(@Validated(OnCreate.class)
                                                                        @RequestBody FlightBookingRequestDTO booking) {
        return ResponseEntity.ok(service.createFlightBooking(booking));
    }
    
    @PutMapping("/flight-booking/edit/{id}")
    public ResponseEntity<Void> updateFlightBooking(@PathVariable Long id,
                                                    @Validated(OnUpdate.class)
                                                    @RequestBody FlightBookingRequestDTO booking) {
        service.updateFlightBooking(id, booking);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/flight-booking/delete/{id}")
    public ResponseEntity<Void> deleteFlightBooking(@PathVariable Long id) {
        service.deleteFlightBooking(id);
        return ResponseEntity.accepted().build();
    }
    
    
}
