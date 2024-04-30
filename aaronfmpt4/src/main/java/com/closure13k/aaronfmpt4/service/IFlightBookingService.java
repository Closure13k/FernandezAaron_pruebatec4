package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.FlightBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightBookingResponseDTO;

import java.util.List;

public interface IFlightBookingService {
    List<FlightBookingResponseDTO> getAllFlightBookings();
    
    FlightBookingResponseDTO getFlightBookingById(Long id);
    
    FlightBookingResponseDTO createFlightBooking(FlightBookingRequestDTO flightBookingDTO);
    
    void updateFlightBooking(Long id, FlightBookingRequestDTO flightBookingDTO);
    
    void deleteFlightBooking(Long id);
}
