package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.response.FlightResponseDTO;
import com.closure13k.aaronfmpt4.exception.EntityNotFoundException;
import com.closure13k.aaronfmpt4.model.Flight;
import com.closure13k.aaronfmpt4.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class FlightServiceTest {
    
    
    @Mock
    private FlightRepository flightRepository;
    
    @InjectMocks
    private FlightService flightService;
    
    
    @Test
    void GivenFlights_WhenGetAllActiveFlights_ThenReturnAllFlights() {
        List<Flight> flights = Arrays.asList(new Flight(), new Flight());
        
        when(flightRepository.findAllActive()).thenReturn(flights);
        
        List<FlightResponseDTO> returnedFlights = flightService.getAllFlights();
        
        assertEquals(2, returnedFlights.size());
    }
    
    @Test
    void GivenNoFlights_WhenGetAllFlights_ThenThrowException() {
        when(flightRepository.findAll()).thenReturn(Collections.emptyList());
        
        assertThrows(EntityNotFoundException.class, () -> flightService.getAllFlights());
    }
    
    @Test
    void GivenFlights_WhenGetFlightsByDateRangeAndLocations_ThenReturnFlights() {
        // Given
        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 12, 31);
        String origin = "Madrid";
        String destination = "Barcelona";
        List<Flight> flights = Arrays.asList(new Flight(), new Flight());
        
        when(flightRepository.findByDateRangeAndLocations(dateFrom, dateTo, origin, destination)).thenReturn(flights);
        
        // When
        List<FlightResponseDTO> returnedFlights = flightService.getFlightsByDateRangeAndLocations(dateFrom, dateTo, origin, destination);
        
        // Then
        assertEquals(2, returnedFlights.size());
    }
    
    @Test
    void GivenNoFlights_WhenGetFlightsByDateRangeAndLocations_ThenThrowException() {
        // Given
        LocalDate dateFrom = LocalDate.of(2022, 1, 1);
        LocalDate dateTo = LocalDate.of(2022, 12, 31);
        String origin = "Madrid";
        String destination = "Barcelona";
        
        when(flightRepository.findByDateRangeAndLocations(dateFrom, dateTo, origin, destination)).thenReturn(Collections.emptyList());
        
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> flightService.getFlightsByDateRangeAndLocations(dateFrom, dateTo, origin, destination));
    }
    
    
}
