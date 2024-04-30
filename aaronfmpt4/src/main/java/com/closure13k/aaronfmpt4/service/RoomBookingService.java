package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.ClientRequestDTO;
import com.closure13k.aaronfmpt4.dto.request.RoomBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.ClientResponseDTO;
import com.closure13k.aaronfmpt4.dto.response.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.response.RoomBookingResponseDTO;
import com.closure13k.aaronfmpt4.dto.response.RoomResponseDTO;
import com.closure13k.aaronfmpt4.exception.DTOValidationException;
import com.closure13k.aaronfmpt4.exception.EntityNotFoundException;
import com.closure13k.aaronfmpt4.model.Client;
import com.closure13k.aaronfmpt4.model.Hotel;
import com.closure13k.aaronfmpt4.model.Room;
import com.closure13k.aaronfmpt4.model.RoomBooking;
import com.closure13k.aaronfmpt4.repository.ClientRepository;
import com.closure13k.aaronfmpt4.repository.RoomBookingRepository;
import com.closure13k.aaronfmpt4.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoomBookingService implements IRoomBookingService {
    
    public static final String ROOM_BOOKING_ENTITY = "Room booking";
    
    private final ClientService clientService;
    
    private final RoomBookingRepository roomBookingRepository;
    
    private final RoomRepository roomRepository;
    
    @Override
    public List<RoomBookingResponseDTO> getAllRoomBookings() {
        List<RoomBooking> bookings = roomBookingRepository.findAll();
        if (bookings.isEmpty()) {
            throw EntityNotFoundException.listIsEmpty(ROOM_BOOKING_ENTITY);
        }
        
        return bookings.stream()
                .map(RoomBookingService::toFullInfoRoomBookingDTO)
                .toList();
    }
    
    @Override
    public RoomBookingResponseDTO getRoomBookingById(Long id) {
        return roomBookingRepository.findById(id)
                .map(this::toBasicRoomBookingDTO)
                .orElseThrow(() -> EntityNotFoundException.byId(ROOM_BOOKING_ENTITY, id));
    }
    
    @Override
    public RoomBookingResponseDTO createRoomBooking(RoomBookingRequestDTO request) {
        Room room = roomRepository.findByCode(request.roomCode())
                .orElseThrow(() -> EntityNotFoundException.byUniqueField("Room", "code", request.roomCode()));
        
        checkIfRequestRangeIsValid(request, room);
        checkIfRequestRangeHasNoBookings(null, request, room);
        
        List<Client> clients = clientService.findOrCreateClients(request.clients());
        
        RoomBooking booking = new RoomBooking();
        booking.setClients(clients);
        booking.setRoom(room);
        booking.setStartDate(request.dateFrom());
        booking.setEndDate(request.dateTo());
        
        return toRoomBookingDTOWithCost(roomBookingRepository.save(booking));
    }
    
    
    @Override
    public void updateRoomBooking(Long id, RoomBookingRequestDTO request) {
        RoomBooking booking = roomBookingRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(ROOM_BOOKING_ENTITY, id));
        
        if (checkAndApplyUpdates(id, request, booking) == 0) {
            throw DTOValidationException.noFieldsToUpdate();
        }
        
        roomBookingRepository.save(booking);
    }
    
    @Override
    public void deleteRoomBooking(Long id) {
        roomBookingRepository.delete(roomBookingRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(ROOM_BOOKING_ENTITY, id)));
    }
    
    private int checkAndApplyUpdates(Long id, RoomBookingRequestDTO request, RoomBooking booking) {
        int updatedFields = 0;
        Room room = booking.getRoom();
        
        if (request.dateFrom() != null && request.dateTo() != null) {
            checkIfRequestRangeIsValid(request, room);
            checkIfRequestRangeHasNoBookings(id, request, room);
            
            booking.setStartDate(request.dateFrom());
            booking.setEndDate(request.dateTo());
            updatedFields++;
        }
        
        List<ClientRequestDTO> clientRequestDTOS = request.clients();
        if (clientRequestDTOS != null && !clientRequestDTOS.isEmpty()) {
            booking.setClients(clientService.findOrCreateClients(clientRequestDTOS));
            updatedFields++;
        }
        return updatedFields;
    }
    
    
    private void checkIfRequestRangeIsValid(RoomBookingRequestDTO request, Room room) {
        if (request.dateFrom().isBefore(room.getAvailableFrom()) || request.dateTo().isAfter(room.getAvailableTo())) {
            throw new DTOValidationException(
                    "Room is not available in the requested dates. Room is available from %s to %s."
                            .formatted(room.getAvailableFrom(), room.getAvailableTo()));
        }
    }
    
    private void checkIfRequestRangeHasNoBookings(Long bookingId, RoomBookingRequestDTO request, Room room) {
        if (roomBookingRepository.isRoomBooked(room.getId(), bookingId, request.dateFrom(), request.dateTo())) {
            throw new DTOValidationException("Room is already booked in the requested dates.");
        }
    }
    
    
    private static RoomBookingResponseDTO toFullInfoRoomBookingDTO(RoomBooking booking) {
        return RoomBookingResponseDTO.builder()
                .withId(booking.getId())
                .withRoom(toBasicRoomDTO(booking.getRoom()))
                .withStartDate(booking.getStartDate())
                .withEndDate(booking.getEndDate())
                .build();
    }
    
    private static RoomResponseDTO toBasicRoomDTO(Room room) {
        return RoomResponseDTO.builder()
                .withCode(room.getCode())
                .build();
    }
    
    private static RoomBookingResponseDTO toRoomBookingDTOWithCost(RoomBooking save) {
        int nights = save.getEndDate().getDayOfYear() - save.getStartDate().getDayOfYear();
        BigDecimal totalPrice = save.getRoom().getPrice().multiply(BigDecimal.valueOf(nights));
        
        return RoomBookingResponseDTO.builder()
                .withNights(nights)
                .withBookingCost(totalPrice)
                .build();
    }
    
    private RoomBookingResponseDTO toBasicRoomBookingDTO(RoomBooking booking) {
        int nights = booking.getEndDate().getDayOfYear() - booking.getStartDate().getDayOfYear();
        BigDecimal totalPrice = booking.getRoom().getPrice().multiply(BigDecimal.valueOf(nights));
        
        return RoomBookingResponseDTO.builder()
                .withRoom(toRoomDTO(booking.getRoom()))
                .withStartDate(booking.getStartDate())
                .withEndDate(booking.getEndDate())
                .withNights(nights)
                .withBookingCost(totalPrice)
                .withClients(clientService.toClientDTOs(booking.getClients()))
                .build();
    }
    
    
    private RoomResponseDTO toRoomDTO(Room room) {
        return RoomResponseDTO.builder()
                .withHotel(toHotelDTO(room.getHotel()))
                .withCode(room.getCode())
                .withType(room.getType())
                .withPrice(room.getPrice())
                .build();
    }
    
    private HotelResponseDTO toHotelDTO(Hotel hotel) {
        return HotelResponseDTO.builder()
                .withName(hotel.getName())
                .withCity(hotel.getCity())
                .build();
    }
    
    
}