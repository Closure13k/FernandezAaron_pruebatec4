package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.FlightRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightResponseDTO;
import com.closure13k.aaronfmpt4.exception.DTOValidationException;
import com.closure13k.aaronfmpt4.exception.EntityNotFoundException;
import com.closure13k.aaronfmpt4.exception.ExistingEntityException;
import com.closure13k.aaronfmpt4.model.Flight;
import com.closure13k.aaronfmpt4.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FlightService implements IFlightService {
    public static final String FLIGHT_ENTITY = "Flight";
    
    private final FlightRepository repository;
    
    /**
     * Get all active flights
     *
     * @throws EntityNotFoundException if no active flights are found
     */
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
    
    
    /**
     * Get flight by id
     *
     * @throws EntityNotFoundException if no flight is found with the given id
     */
    @Override
    public FlightResponseDTO getFlightById(Long id) {
        return repository.findActiveById(id)
                .map(FlightService::toBasicFlightDTO)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_ENTITY, id));
    }
    
    /**
     * Create a new flight
     *
     * @throws ExistingEntityException if a flight with the same code already exists
     */
    @Override
    public FlightResponseDTO createFlight(FlightRequestDTO flightDTO) {
        repository.findByCode(flightDTO.code())
                .ifPresent(flight -> {
                    throw ExistingEntityException.byUniqueField(FLIGHT_ENTITY, "code", flightDTO.code());
                });
        
        return toBasicFlightDTOWithId(repository.save(fromDTO(flightDTO)));
        
    }
    
    
    /**
     * Create a list of flights
     *
     * @throws ExistingEntityException if a flight with the same code already exists
     */
    @Override
    public List<FlightResponseDTO> createFlightsFromList(List<FlightRequestDTO> flightDTOs) {
        List<Flight> flights = repository.saveAll(flightDTOs.stream()
                .map(this::fromDTO)
                .toList());
        
        return flights.stream()
                .map(this::toBasicFlightDTOWithId)
                .toList();
    }
    
    /**
     * Update flight
     *
     * @throws EntityNotFoundException if no flight is found with the given id
     * @throws DTOValidationException  if no fields are updated
     */
    @Override
    public FlightResponseDTO updateFlight(Long id, FlightRequestDTO flightDTO) {
        Flight flight = repository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_ENTITY, id));
        
        int updates = checkAndApplyUpdates(flightDTO, flight);
        if (updates == 0) {
            throw DTOValidationException.noFieldsToUpdate();
        }
        
        Flight save = repository.save(flight);
        return toBasicFlightDTOWithId(save);
    }
    
    
    /**
     * Delete flight
     *
     * @throws EntityNotFoundException if no flight is found with the given id
     */
    @Override
    public void deleteFlight(Long id) {
        Flight flight = repository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_ENTITY, id));
        
        flight.setIsRemoved(true);
        repository.save(flight);
    }
    
    /**
     * Get flights by date range and locations
     *
     * @throws EntityNotFoundException if no flights are found with the given parameters
     */
    @Override
    public List<FlightResponseDTO> getFlightsByDateRangeAndLocations(LocalDate dateFrom, LocalDate dateTo, String origin, String destination) {
        
        List<Flight> flights = repository.findByDateRangeAndLocations(dateFrom, dateTo, origin, destination);
        if (flights.isEmpty()) {
            throw EntityNotFoundException.listIsEmpty(FLIGHT_ENTITY);
        }
        return flights.stream()
                .map(FlightService::toBasicFlightDTO)
                .toList();
        
        
    }
    
    
    /**
     * Apply updates to the flight.
     *
     * @return the number of updates applied.
     */
    private int checkAndApplyUpdates(FlightRequestDTO request, Flight flight) {
        int updates = 0;
        String code = request.code();
        if (isCodeValidAndUpdated(code, flight.getCode())) {
            flight.setCode(code);
            updates++;
        }
        
        String origin = request.origin();
        if (isFieldUpdated(origin)) {
            flight.setOrigin(origin);
            updates++;
        }
        
        
        String destination = request.destination();
        if (isFieldUpdated(destination)) {
            flight.setDestination(destination);
            updates++;
        }
        
        LocalDate departureDate = request.departureDate();
        if (departureDate != null) {
            flight.setDepartureDate(departureDate);
            updates++;
        }
        
        Integer availableSeats = request.availableSeats();
        if (availableSeats != null) {
            flight.setAvailableSeats(availableSeats);
            updates++;
        }
        
        BigDecimal price = request.price();
        if (price != null) {
            flight.setPrice(price);
            updates++;
        }
        
        return updates;
    }
    
    /**
     * Check if the code provided is valid and was updated.
     *
     * @return true if the code is valid. False otherwise.
     * @throws ExistingEntityException if a flight with the same code already exists
     * @throws DTOValidationException  if the code is empty
     */
    private boolean isCodeValidAndUpdated(String newCode, String currentCode) {
        if (newCode != null && !newCode.isBlank() && !newCode.equals(currentCode)) {
            repository.findByCode(newCode)
                    .ifPresent(f -> {
                        throw ExistingEntityException.byUniqueField(FLIGHT_ENTITY, "code", newCode);
                    });
            return true;
        }
        return false;
    }
    
    /**
     * Check if the field was updated.
     */
    private static boolean isFieldUpdated(String destination) {
        return destination != null && !destination.isBlank();
    }
    
    /**
     * Convert a flight to a basic flight DTO (code, origin, destination, departureDate, availableSeats, price)
     */
    private static FlightResponseDTO toBasicFlightDTO(Flight flight) {
        return FlightResponseDTO.builder()
                .withCode(flight.getCode())
                .withOrigin(flight.getOrigin())
                .withDestination(flight.getDestination())
                .withDepartureDate(String.valueOf(flight.getDepartureDate()))
                .withAvailableSeats(flight.getAvailableSeats())
                .withPrice(flight.getPrice())
                .build();
    }
    
    /**
     * Convert a flight to a basic flight DTO with id (id, code, origin, destination, departureDate, availableSeats, price)
     */
    private FlightResponseDTO toBasicFlightDTOWithId(Flight flight) {
        return FlightResponseDTO.builder()
                .withId(flight.getId())
                .withCode(flight.getCode())
                .withOrigin(flight.getOrigin())
                .withDestination(flight.getDestination())
                .withDepartureDate(String.valueOf(flight.getDepartureDate()))
                .withAvailableSeats(flight.getAvailableSeats())
                .withPrice(flight.getPrice())
                .build();
    }
    
    /**
     * Convert a flight DTO to a flight entity. Generate a code if the code is empty.
     */
    private Flight fromDTO(FlightRequestDTO flightDTO) {
        Flight flight = new Flight();
        flight.setOrigin(flightDTO.origin());
        flight.setDestination(flightDTO.destination());
        flight.setDepartureDate(flightDTO.departureDate());
        flight.setAvailableSeats(flightDTO.availableSeats());
        flight.setPrice(flightDTO.price());
        if (flightDTO.code() == null || flightDTO.code().isBlank()) {
            flight.generateCode();
        } else {
            flight.setCode(flightDTO.code());
        }
        
        return flight;
    }
}