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

@RequiredArgsConstructor
@Service
public class FlightBookingService implements IFlightBookingService {
    
    public static final String FLIGHT_BOOKING_ENTITY = "Flight Booking";
    
    private final ClientService clientService;
    
    private final FlightBookingRepository flightBookingRepository;
    
    private final FlightRepository flightRepository;
    
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
    
    @Override
    public FlightBookingResponseDTO getFlightBookingById(Long id) {
        return flightBookingRepository.findById(id)
                .map(FlightBookingService::toBasicDTO)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_BOOKING_ENTITY, id));
    }
    
    @Override
    public FlightBookingResponseDTO createFlightBooking(FlightBookingRequestDTO request) {
        Flight flight = flightRepository.findByCode(request.flightCode())
                .orElseThrow(() -> EntityNotFoundException.byUniqueField("Flight", "code", request.flightCode()));
        
        
        Client client = clientService.findOrCreateClient(request.client());
        
        FlightBooking booking = new FlightBooking();
        booking.setCode(request.flightCode());
        booking.setSeatType(request.seatType());
        booking.setClient(client);
        booking.setFlight(flight);
        
        return toBasicDTO(flightBookingRepository.save(booking));
    }
    
    @Override
    public void updateFlightBooking(Long id, FlightBookingRequestDTO flightBookingDTO) {
        FlightBooking booking = flightBookingRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(FLIGHT_BOOKING_ENTITY, id));
        
        if (checkAndApplyUpdates(booking, flightBookingDTO) == 0) {
            throw DTOValidationException.noFieldsToUpdate();
        }
        flightBookingRepository.save(booking);
    }
    
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
    
    @Override
    public void deleteFlightBooking(Long id) {
        flightBookingRepository.findById(id)
                .ifPresentOrElse(
                        flightBookingRepository::delete,
                        () -> {
                            throw EntityNotFoundException.byId(FLIGHT_BOOKING_ENTITY, id);
                        });
        
    }
    
    
    // ------ Entity to DTO conversion methods ------
    private static FlightBookingResponseDTO toBasicDTO(FlightBooking flightBooking) {
        return FlightBookingResponseDTO.builder()
                .withCode(flightBooking.getCode())
                .withSeatType(flightBooking.getSeatType())
                .withClient(toClientDTO(flightBooking.getClient()))
                .withFlight(toFlightDTO(flightBooking.getFlight()))
                .build();
    }
    
    
    private static ClientResponseDTO toClientDTO(Client client) {
        return ClientResponseDTO.builder()
                .withNif(client.getNif())
                .withName(client.getName())
                .withSurname(client.getSurname())
                .build();
    }
    
    private static FlightResponseDTO toFlightDTO(Flight flight) {
        return FlightResponseDTO.builder()
                .withCode(flight.getCode())
                .withOrigin(flight.getOrigin())
                .withDestination(flight.getDestination())
                .build();
    }
}
