package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.request.FlightBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightBookingResponseDTO;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import com.closure13k.aaronfmpt4.service.IFlightBookingService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling HTTP requests related to flight bookings.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class FlightBookingController {
    private final IFlightBookingService service;
    
    /**
     * Get all flight bookings.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved flight bookings"),
            @ApiResponse(responseCode = "404", description = "If the list of flight bookings is empty.")
    })
    @GetMapping("/flight-booking")
    public ResponseEntity<List<FlightBookingResponseDTO>> getFlightBookings() {
        return ResponseEntity.ok(service.getAllFlightBookings());
    }
    
    /**
     * Get a flight booking by id.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a flight booking by id."),
            @ApiResponse(responseCode = "404", description = "If the flight booking with the given id does not exist."),
    })
    @GetMapping("/flight-booking/{id}")
    public ResponseEntity<FlightBookingResponseDTO> getFlightBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFlightBookingById(id));
    }
    
    /**
     * Create a new flight booking.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new flight booking."),
            @ApiResponse(responseCode = "400", description = "If the request is invalid."),
            @ApiResponse(responseCode = "404", description = "If the flight with the given code does not exist."),
    })
    @PostMapping("/flight-booking/new")
    public ResponseEntity<FlightBookingResponseDTO> createFlightBooking(@Validated(OnCreate.class)
                                                                        @RequestBody FlightBookingRequestDTO booking) {
        return ResponseEntity.ok(service.createFlightBooking(booking));
    }
    
    
    /**
     * Update a flight booking.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated a flight booking."),
            @ApiResponse(responseCode = "400", description = "If the request is invalid."),
            @ApiResponse(responseCode = "404", description = "If the flight booking with the given id does not exist."),
    })
    @PutMapping("/flight-booking/edit/{id}")
    public ResponseEntity<Void> updateFlightBooking(@PathVariable Long id,
                                                    @Validated(OnUpdate.class)
                                                    @RequestBody FlightBookingRequestDTO booking) {
        service.updateFlightBooking(id, booking);
        return ResponseEntity.ok().build();
    }
    
    
    /**
     * Delete a flight booking.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Successfully deleted a flight booking."),
            @ApiResponse(responseCode = "404", description = "If the flight booking with the given id does not exist."),
    })
    @DeleteMapping("/flight-booking/delete/{id}")
    public ResponseEntity<Void> deleteFlightBooking(@PathVariable Long id) {
        service.deleteFlightBooking(id);
        return ResponseEntity.accepted().build();
    }
    
    
}
