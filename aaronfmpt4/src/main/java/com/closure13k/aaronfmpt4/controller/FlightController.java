package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.request.FlightRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightResponseDTO;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import com.closure13k.aaronfmpt4.service.IFlightService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    
    @GetMapping("/flight-search")
    public ResponseEntity<List<FlightResponseDTO>> getFlightsByDateAndLocation(
            @DateTimeFormat(fallbackPatterns = {"yyyy/MM/dd", "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd"})
            @RequestParam LocalDate dateFrom,
            @DateTimeFormat(fallbackPatterns = {"yyyy/MM/dd", "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd"})
            @RequestParam LocalDate dateTo,
            @RequestParam String origin,
            @RequestParam String destination) {
        return ResponseEntity.ok(service.getFlightsByDateRangeAndLocations(dateFrom, dateTo, origin, destination));
    }
    
    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@NotNull @Positive @PathVariable Long id) {
        return ResponseEntity.ok(service.getFlightById(id));
    }
    
    @PostMapping("/flights/new")
    public ResponseEntity<FlightResponseDTO> createFlight(@Validated(OnCreate.class) @RequestBody FlightRequestDTO flight) {
        return ResponseEntity.ok(service.createFlight(flight));
    }
    
    @PostMapping("/flights/newbatch")
    public ResponseEntity<List<FlightResponseDTO>> createFlights(@RequestBody List<FlightRequestDTO> flightDTOs) {
        return ResponseEntity.ok(service.createFlightsFromList(flightDTOs));
    }
    
    @PutMapping("/flights/edit/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(@NotNull @Positive @PathVariable Long id,
                                                          @Validated(OnUpdate.class) @RequestBody FlightRequestDTO flight) {
        service.updateFlight(id, flight);
        return ResponseEntity.ok(service.getFlightById(id));
    }
    
    @DeleteMapping("/flights/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@Positive @PathVariable Long id) {
        service.deleteFlight(id);
        return ResponseEntity.accepted().build();
    }
    
    
}
