package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.ClientRequestDTO;
import com.closure13k.aaronfmpt4.dto.request.FlightBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.ClientResponseDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightBookingResponseDTO;
import com.closure13k.aaronfmpt4.dto.response.FlightResponseDTO;
import com.closure13k.aaronfmpt4.exception.DTOValidationException;
import com.closure13k.aaronfmpt4.exception.EntityNotFoundException;
import com.closure13k.aaronfmpt4.model.Client;
import com.closure13k.aaronfmpt4.model.Flight;
import com.closure13k.aaronfmpt4.model.FlightBooking;
import com.closure13k.aaronfmpt4.repository.FlightBookingRepository;
import com.closure13k.aaronfmpt4.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing flight bookings.
 */
@RequiredArgsConstructor
@Service
public class FlightBookingService implements IFlightBookingService {
    
    public static final String FLIGHT_BOOKING_ENTITY = "Flight Booking";
    
    private final ClientService clientService;
    
    private final FlightBookingRepository flightBookingRepository;
    
    private final FlightRepository flightRepository;
    
    /**
     * Get all flight bookings.
     *
     * @throws EntityNotFoundException if there are no bookings.
     */
    @Override
    public List<FlightBookingResponseDTO> getAllFlightBookings() {
        List<FlightBooking> bookings = flightBookingRepository.findAll();
        if (bookings.isEmpty()) {
            throw EntityNotFoundException.listIsEmpty(FLIGHT_BOOKING_ENTITY);
        }
        return bookings.stream()
                .map(FlightBookingService::toBasicDTO)
                .toList();
    }
    
    /**
     * Get a flight booking by its ID.
     *
     * @throws EntityNotFoundException if the booking does not exist.
     */
    @Override
    public FlightBookingResponseDTO getFlightBookingById(Long id) {
        return flightBookingRepository.findById(id)
                .map(FlightBookingService::toBasicDTO)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_BOOKING_ENTITY, id));
    }
    
    /**
     * Create a new flight booking.
     *
     * @throws EntityNotFoundException if the flight does not exist.
     * @throws DTOValidationException  if there are no available seats for the flight.
     * @throws DTOValidationException  if the client has already booked the flight.
     */
    @Override
    public FlightBookingResponseDTO createFlightBooking(FlightBookingRequestDTO request) {
        Flight flight = flightRepository.findByCode(request.flightCode())
                .orElseThrow(() -> EntityNotFoundException.byUniqueField("Flight", "code", request.flightCode()));
        
        Integer availableSeats = flight.getAvailableSeats();
        if (availableSeats <= 0) {
            throw new DTOValidationException("No available seats for flight " + flight.getCode());
        }
        
        Client client = clientService.findOrCreateClient(request.client());
        
        flightBookingRepository.findByClientAndFlight(client, flight)
                .ifPresent(booking -> {
                    throw new DTOValidationException("Client has already booked this flight");
                });
        
        FlightBooking booking = new FlightBooking();
        if (request.code() != null) {
            booking.setCode(request.code());
        } else {
            booking.setCode(request.flightCode() + "-" + client.getNif());
        }
        booking.setSeatType(request.seatType());
        booking.setClient(client);
        booking.setFlight(flight);
        
        FlightBooking save = flightBookingRepository.save(booking);
        
        flight.setAvailableSeats(availableSeats - 1);
        flightRepository.save(flight);
        
        return toBasicDTO(save);
    }
    
    /**
     * Update a flight booking.
     *
     * @throws EntityNotFoundException if the booking does not exist.
     */
    @Override
    public void updateFlightBooking(Long id, FlightBookingRequestDTO flightBookingDTO) {
        FlightBooking booking = flightBookingRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_BOOKING_ENTITY, id));
        
        if (checkAndApplyUpdates(booking, flightBookingDTO) == 0) {
            throw DTOValidationException.noFieldsToUpdate();
        }
        flightBookingRepository.save(booking);
    }
    
    /**
     * Delete a flight booking by its ID.
     *
     * @throws EntityNotFoundException if the booking does not exist.
     */
    @Override
    public void deleteFlightBooking(Long id) {
        FlightBooking booking = flightBookingRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_BOOKING_ENTITY, id));
        flightBookingRepository.deleteById(id);
        
        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);
    }
    
    /**
     * Check and apply updates to a flight booking.
     *
     * @param booking The booking to update.
     * @param request The request DTO with the new data.
     * @return The number of fields updated.
     * @throws EntityNotFoundException if the flight does not exist.
     */
    private int checkAndApplyUpdates(FlightBooking booking, FlightBookingRequestDTO request) {
        int updates = 0;
        
        String flightCode = request.flightCode();
        if (flightCode != null && !flightCode.equals(booking.getFlight().getCode())) {
            Flight newFlight = flightRepository.findByCode(flightCode)
                    .orElseThrow(() -> EntityNotFoundException.byUniqueField("Flight", "code", flightCode));
            booking.setFlight(newFlight);
            updates++;
        }
        
        String seatType = request.seatType();
        if (seatType != null && !seatType.equals(booking.getSeatType())) {
            booking.setSeatType(request.seatType());
            updates++;
        }
        
        ClientRequestDTO client = request.client();
        if (client != null) {
            Client newClient = clientService.findOrCreateClient(client);
            if (!newClient.equals(booking.getClient())) {
                booking.setClient(newClient);
                updates++;
            }
        }
        
        return updates;
    }
    
    /**
     * Convert a flight booking entity to a basic DTO (code, seat type, client, flight).
     */
    private static FlightBookingResponseDTO toBasicDTO(FlightBooking flightBooking) {
        return FlightBookingResponseDTO.builder()
                .withCode(flightBooking.getCode())
                .withSeatType(flightBooking.getSeatType())
                .withClient(toClientDTO(flightBooking.getClient()))
                .withFlight(toFlightDTO(flightBooking.getFlight()))
                .build();
    }
    
    /**
     * Convert a client entity to a DTO.
     */
    private static ClientResponseDTO toClientDTO(Client client) {
        return ClientResponseDTO.builder()
                .withNif(client.getNif())
                .withName(client.getName())
                .withSurname(client.getSurname())
                .build();
    }
    
    /**
     * Convert a flight entity to a DTO.
     */
    private static FlightResponseDTO toFlightDTO(Flight flight) {
        return FlightResponseDTO.builder()
                .withCode(flight.getCode())
                .withOrigin(flight.getOrigin())
                .withDestination(flight.getDestination())
                .withPrice(flight.getPrice())
                .build();
    }
}
