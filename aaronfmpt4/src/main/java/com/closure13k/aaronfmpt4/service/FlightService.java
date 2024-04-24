package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.FlightRequestDTO;
import com.closure13k.aaronfmpt4.dto.FlightResponseDTO;
import com.closure13k.aaronfmpt4.exception.EntityNotFoundException;
import com.closure13k.aaronfmpt4.exception.ExistingEntityException;
import com.closure13k.aaronfmpt4.model.Flight;
import com.closure13k.aaronfmpt4.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FlightService implements IFlightService {
    
    // Constant representing the entity name
    public static final String FLIGHT_ENTITY = "Flight";
    private final FlightRepository repository;
    
    @Override
    public List<FlightResponseDTO> getAllFlights() {
        List<Flight> allActiveFlights = repository.findAllActive();
        if (allActiveFlights.isEmpty()) {
            throw EntityNotFoundException.listIsEmpty("Flights");
        }
        
        return allActiveFlights.stream()
                .map(FlightService::toBasicFlightDTO)
                .toList();
    }
    
    
    @Override
    public FlightResponseDTO getFlightById(Long id) {
        return repository.findActiveById(id)
                .map(FlightService::toBasicFlightDTO)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_ENTITY, id));
    }
    
    @Override
    public FlightResponseDTO createFlight(FlightRequestDTO flightDTO) {
        repository.findByCode(flightDTO.code())
                .ifPresent(flight -> {
                    throw ExistingEntityException.byUniqueField(FLIGHT_ENTITY, "code", flightDTO.code());
                });
        
        Flight flight = this.fromDTO(flightDTO);
        repository.save(flight);
        
        return toBasicFlightDTOWithId(flight);
        
    }
    
    
    @Override
    public List<FlightResponseDTO> createFlightsFromList(List<FlightRequestDTO> flightDTOs) {
        List<Flight> flights = repository.saveAll(flightDTOs.stream()
                .map(this::fromDTO)
                .toList());
        
        return flights.stream()
                .map(this::toBasicFlightDTOWithId)
                .toList();
    }
    
    @Override
    public void updateFlight(Long id, FlightRequestDTO flightDTO) {
        Flight flight = repository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_ENTITY, id));
        
        String code = flightDTO.code();
        if (code != null && !code.isBlank() && !code.equals(flight.getCode())) {
            repository.findByCode(code)
                    .ifPresent(f -> {
                        throw ExistingEntityException.byUniqueField(FLIGHT_ENTITY, "code", code);
                    });
            flight.setCode(code);
        }
        
        String origin = flightDTO.origin();
        if (origin != null && !origin.isBlank()) {
            flight.setOrigin(origin);
        }
        
        String destination = flightDTO.destination();
        if (destination != null && !destination.isBlank()) {
            flight.setDestination(destination);
        }
        
        LocalDate departureDate = flightDTO.departureDate();
        if (departureDate != null) {
            flight.setDepartureDate(departureDate);
        }
        
        String seatType = flightDTO.seatType();
        if (seatType != null && !seatType.isBlank()) {
            flight.setSeatType(seatType);
        }
        
        Integer availableSeats = flightDTO.availableSeats();
        if (availableSeats != null) {
            flight.setAvailableSeats(availableSeats);
        }
        
        BigDecimal price = flightDTO.price();
        if (price != null) {
            flight.setPrice(price);
        }
        
        repository.save(flight);
    }
    
    @Override
    public void deleteFlight(Long id) {
        Flight flight = repository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_ENTITY, id));
        
        if (Boolean.TRUE.equals(flight.getIsRemoved())) {
            throw EntityNotFoundException.byId(FLIGHT_ENTITY, id);
        }
        
        flight.setIsRemoved(true);
        repository.save(flight);
    }
    
    
    private static FlightResponseDTO toBasicFlightDTO(Flight flight) {
        return FlightResponseDTO.builder()
                .withCode(flight.getCode())
                .withOrigin(flight.getOrigin())
                .withDestination(flight.getDestination())
                .withDepartureDate(String.valueOf(flight.getDepartureDate()))
                .withSeatType(flight.getSeatType())
                .withAvailableSeats(flight.getAvailableSeats())
                .withPrice(NumberFormat.getCurrencyInstance().format(flight.getPrice()))
                .build();
    }
    
    private FlightResponseDTO toBasicFlightDTOWithId(Flight flight) {
        return FlightResponseDTO.builder()
                .withId(flight.getId())
                .withCode(flight.getCode())
                .withOrigin(flight.getOrigin())
                .withDestination(flight.getDestination())
                .withDepartureDate(String.valueOf(flight.getDepartureDate()))
                .withSeatType(flight.getSeatType())
                .withAvailableSeats(flight.getAvailableSeats())
                .withPrice(NumberFormat.getCurrencyInstance().format(flight.getPrice()))
                .build();
    }
    
    private Flight fromDTO(FlightRequestDTO flightDTO) {
        Flight flight = new Flight();
        flight.setCode(flightDTO.code());
        flight.setOrigin(flightDTO.origin());
        flight.setDestination(flightDTO.destination());
        flight.setDepartureDate(flightDTO.departureDate());
        flight.setSeatType(flightDTO.seatType());
        flight.setAvailableSeats(flightDTO.availableSeats());
        flight.setPrice(flightDTO.price());
        
        return flight;
    }
}