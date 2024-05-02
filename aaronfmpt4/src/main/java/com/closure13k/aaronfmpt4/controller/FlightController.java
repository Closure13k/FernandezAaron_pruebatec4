package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.request.FlightRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightResponseDTO;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import com.closure13k.aaronfmpt4.service.IFlightService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller class for handling flight-related requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class FlightController {
    private final IFlightService service;
    
    /**
     * Get all flights.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of flights"),
            @ApiResponse(responseCode = "404", description = "No flights found.")
    })
    @GetMapping("/flights")
    public ResponseEntity<List<FlightResponseDTO>> getFlights() {
        return ResponseEntity.ok(service.getAllFlights());
    }
    
    /**
     * Get flights by date range and locations.
     * Also provides the returning flights for the same date range and locations.
     * @param dateFrom the start date.
     * @param dateTo the end date.
     * @param origin the origin location.
     * @param destination the destination location.
     * @return a list of flights that match the criteria.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of flights"),
            @ApiResponse(responseCode = "404", description = "No flights found.")
    })
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
    
    /**
     * Get a flight by its ID.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved flight"),
            @ApiResponse(responseCode = "404", description = "Flight not found.")
    })
    @GetMapping("/flights/{id}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(service.getFlightById(id));
    }
    
    /**
     * Create a new flight.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created flight"),
            @ApiResponse(responseCode = "400", description = "If the request body is invalid."),
            @ApiResponse(responseCode = "409", description = "Flight already exists.")
    })
    @PostMapping("/flights/new")
    public ResponseEntity<FlightResponseDTO> createFlight(@Validated(OnCreate.class) @RequestBody FlightRequestDTO flight) {
        return ResponseEntity.ok(service.createFlight(flight));
    }
    
    /**
     * Create multiple flights.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created flights"),
            @ApiResponse(responseCode = "400", description = "If the request body is invalid.")
    })
    @PostMapping("/flights/newbatch")
    public ResponseEntity<List<FlightResponseDTO>> createFlights(@RequestBody List<FlightRequestDTO> flightDTOs) {
        return ResponseEntity.ok(service.createFlightsFromList(flightDTOs));
    }
    
    /**
     * Update a flight.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated flight"),
            @ApiResponse(responseCode = "400", description = "If the request body is invalid."),
            @ApiResponse(responseCode = "404", description = "Flight not found.")
    })
    @PutMapping("/flights/edit/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(@Positive @PathVariable Long id,
                                                          @Validated(OnUpdate.class) @RequestBody FlightRequestDTO flight) {
        service.updateFlight(id, flight);
        return ResponseEntity.ok(service.getFlightById(id));
    }
    
    /**
     * Delete a flight.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully deleted flight"),
            @ApiResponse(responseCode = "404", description = "Flight not found.")
    })
    @DeleteMapping("/flights/delete/{id}")
    public ResponseEntity<Void> deleteFlight(@Positive @PathVariable Long id) {
        service.deleteFlight(id);
        return ResponseEntity.accepted().build();
    }
    
    
}
