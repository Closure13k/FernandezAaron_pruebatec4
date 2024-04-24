package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.FlightRequestDTO;
import com.closure13k.aaronfmpt4.dto.FlightResponseDTO;

import java.util.List;

public interface IFlightService {
    List<FlightResponseDTO> getAllFlights();
    
    FlightResponseDTO getFlightById(Long id);
    
    FlightResponseDTO createFlight(FlightRequestDTO flightDTO);
    
    List<FlightResponseDTO> createFlightsFromList(List<FlightRequestDTO> flightDTOs);
    
    void updateFlight(Long id, FlightRequestDTO flightDTO);
    
    void deleteFlight(Long id);
}
