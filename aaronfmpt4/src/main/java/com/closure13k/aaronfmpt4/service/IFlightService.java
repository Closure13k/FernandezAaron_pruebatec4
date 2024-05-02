package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.FlightRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface IFlightService {
    List<FlightResponseDTO> getAllFlights();
    
    FlightResponseDTO getFlightById(Long id);
    
    FlightResponseDTO createFlight(FlightRequestDTO flightDTO);
    
    List<FlightResponseDTO> createFlightsFromList(List<FlightRequestDTO> flightDTOs);
    
    FlightResponseDTO updateFlight(Long id, FlightRequestDTO flightDTO);
    
    void deleteFlight(Long id);
    
    List<FlightResponseDTO> getFlightsByDateRangeAndLocations(LocalDate dateFrom, LocalDate dateTo, String origin, String destination);
}
