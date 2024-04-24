package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.FlightRequestDTO;
import com.closure13k.aaronfmpt4.dto.FlightResponseDTO;
import com.closure13k.aaronfmpt4.service.IFlightService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class FlightController {
    private final IFlightService service;
    
    @GetMapping("/flights")
    public ResponseEntity<List<FlightResponseDTO>> getFlights() {
        return ResponseEntity.ok(service.getAllFlights());
    }
    
    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@NotNull @Positive @PathVariable Long id) {
        return ResponseEntity.ok(service.getFlightById(id));
    }
    
    @PostMapping("/flights/new")
    public ResponseEntity<FlightResponseDTO> createFlight(
            @Valid
            @RequestBody FlightRequestDTO flightDTO) {
        return ResponseEntity.ok(service.createFlight(flightDTO));
    }
    
    @PostMapping("/flights/newbatch")
    public ResponseEntity<List<FlightResponseDTO>> createFlights(@RequestBody List<FlightRequestDTO> flightDTOs) {
        return ResponseEntity.ok(service.createFlightsFromList(flightDTOs));
    }
    
    @PutMapping("/flights/edit/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(
            @NotNull @Positive @PathVariable Long id,
            @Valid @RequestBody FlightRequestDTO flightDTO) {
        return ResponseEntity.ok(service.updateFlight(id, flightDTO));
    }
    
    @DeleteMapping("/flights/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@NotNull @Positive @PathVariable Long id) {
        service.deleteFlight(id);
        return ResponseEntity.accepted().build();
    }
    
    
    
}
