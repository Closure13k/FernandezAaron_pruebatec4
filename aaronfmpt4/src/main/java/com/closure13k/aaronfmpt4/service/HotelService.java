package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomResponseDTO;
import com.closure13k.aaronfmpt4.exception.ExistingEntityException;
import com.closure13k.aaronfmpt4.exception.HotelNotFoundException;
import com.closure13k.aaronfmpt4.exception.RoomNotFoundException;
import com.closure13k.aaronfmpt4.model.Hotel;
import com.closure13k.aaronfmpt4.model.Room;
import com.closure13k.aaronfmpt4.repository.HotelRepository;
import com.closure13k.aaronfmpt4.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HotelService implements IHotelService {
    
    private final HotelRepository hotelRepository;
    
    private final RoomRepository roomRepository;
    
    @Override
    public List<HotelResponseDTO> getAllHotels() {
        List<Hotel> allActiveHotels = hotelRepository.findAllActive();
        if (allActiveHotels.isEmpty()) {
            throw HotelNotFoundException.listIsEmpty();
        }
        
        return allActiveHotels.stream()
                .map(HotelService::toBasicHotelDTO)
                .toList();
    }
    
    
    @Override
    public HotelResponseDTO getHotelById(Long id) {
        return hotelRepository.findActiveById(id)
                .map(HotelService::toBasicHotelDTO)
                .orElseThrow(() -> HotelNotFoundException.byId(id));
    }
    
    
    @Override
    public HotelResponseDTO createHotel(HotelRequestDTO hotelDTO) {
        hotelRepository.findByCode(hotelDTO.code())
                .ifPresent(hotel -> {
                    throw ExistingEntityException.byUniqueField("Hotel", "code", hotelDTO.code());
                });
        
        Hotel hotel = this.fromDTO(hotelDTO);
        hotelRepository.save(hotel);
        
        return toBasicHotelDTOWithId(hotel);
    }
    
    
    @Override
    public List<HotelResponseDTO> createHotelsFromList(List<HotelRequestDTO> hotelDTOs) {
        
        List<Hotel> hotels = hotelRepository.saveAll(
                hotelDTOs.stream()
                        .map(this::fromDTO)
                        .toList()
        );
        
        return hotels.stream()
                .map(HotelService::toBasicHotelDTOWithId)
                .toList();
    }
    
    @Override
    public void updateHotel(Long id, HotelRequestDTO hotelDTO) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> HotelNotFoundException.byId(id));
        
        
        String code = hotelDTO.code();
        if (code != null && !code.isBlank()) {
            hotel.setCode(code);
        }
        
        String name = hotelDTO.name();
        if (name != null && !name.isBlank()) {
            hotel.setName(name);
        }
        
        String city = hotelDTO.city();
        if (city != null && !city.isBlank()) {
            hotel.setCity(city);
        }
        
        hotelRepository.save(hotel);
    }
    
    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> HotelNotFoundException.byId(id));
        if (Boolean.TRUE.equals(hotel.getIsRemoved())) {
            throw HotelNotFoundException.byId(id);
        }
        
        if (hasActiveChildren(hotel)) {
            throw ExistingEntityException.hasActiveChildren("Hotel", "rooms", id);
        }
        
        hotel.setIsRemoved(true);
        hotelRepository.save(hotel);
    }
    
    private static boolean hasActiveChildren(Hotel hotel) {
        return hotel.getRooms().stream().anyMatch(room -> !room.getIsRemoved());
    }
    
    @Override
    public List<RoomResponseDTO> getRoomsByHotelId(Long id) {
        List<Room> rooms = roomRepository.findByHotelId(id);
        if (rooms.isEmpty()) {
            throw RoomNotFoundException.listIsEmpty();
        }
        
        return rooms.stream()
                .map(HotelService::toBasicRoomDTO)
                .toList();
    }
    
    
    @Override
    public RoomResponseDTO getRoomByHotelIdAndRoomId(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> HotelNotFoundException.byId(hotelId));
        
        return hotel.getRooms().stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .map(HotelService::toBasicRoomDTO)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
    }
    
    @Override
    public RoomResponseDTO createRoom(Long id, RoomRequestDTO roomDTO) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> HotelNotFoundException.byId(id));
        
        Room room = fromDTO(roomDTO, hotel);
        roomRepository.save(room);
        
        return toBasicRoomDTO(room);
    }
    
    
    @Override
    public void updateRoom(Long hotelId, Long roomId, RoomRequestDTO roomDTO) {
        Room room = roomRepository.findByHotelIdAndRoomId(hotelId, roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        
        room.setCode(roomDTO.code());
        room.setType(roomDTO.type());
        room.setAvailableFrom(roomDTO.availableFrom());
        room.setAvailableTo(roomDTO.availableTo());
        room.setPrice(roomDTO.price());
        
        roomRepository.save(room);
    }
    
    @Override
    public RoomResponseDTO deleteRoom(Long hotelId, Long roomId) {
        Room room = roomRepository.findByHotelIdAndRoomId(hotelId, roomId)
                .orElseThrow(() -> RoomNotFoundException.byId(roomId));
        
        if (Boolean.TRUE.equals(room.getIsRemoved())) {
            throw RoomNotFoundException.byId(roomId);
        }
        
        room.setIsRemoved(true);
        roomRepository.save(room);
        
        return toBasicRoomDTO(room);
    }
    
    @Override
    public List<RoomResponseDTO> getRoomsByDateAndDestination(LocalDate startDate, LocalDate endDate, String destination) {
        List<Room> rooms = roomRepository
                .findByDateRangeAndDestination(startDate, endDate, destination);
        if (rooms.isEmpty()) {
            throw RoomNotFoundException.listIsEmpty();
        }
        
        return rooms.stream()
                .map(HotelService::toBasicRoomDTO)
                .toList();
    }
    
    
    private static HotelResponseDTO toBasicHotelDTOWithId(Hotel hotel) {
        return HotelResponseDTO.builder()
                .withId(hotel.getId())
                .withCode(hotel.getCode())
                .withName(hotel.getName())
                .withCity(hotel.getCity())
                .build();
    }
    
    
    private static HotelResponseDTO toBasicHotelDTO(Hotel hotel) {
        return HotelResponseDTO.builder()
                .withCode(hotel.getCode())
                .withName(hotel.getName())
                .withCity(hotel.getCity())
                .build();
    }
    
    private static RoomResponseDTO toBasicRoomDTO(Room room) {
        return RoomResponseDTO.builder()
                .withCode(room.getCode())
                .withType(room.getType())
                .withAvailableFrom(room.getAvailableFrom().toString())
                .withAvailableTo(room.getAvailableTo().toString())
                .withPrice(formatMonetaryValue(room.getPrice()))
                .build();
    }
    
    
    private Hotel fromDTO(HotelRequestDTO requestDTO) {
        Hotel hotel = new Hotel();
        hotel.setCode(requestDTO.code());
        hotel.setName(requestDTO.name());
        hotel.setCity(requestDTO.city());
        hotel.setIsRemoved(false);
        return hotel;
    }
    
    private static Room fromDTO(RoomRequestDTO roomDTO, Hotel hotel) {
        Room room = new Room();
        room.setCode(roomDTO.code());
        room.setType(roomDTO.type());
        room.setAvailableFrom(roomDTO.availableFrom());
        room.setAvailableTo(roomDTO.availableTo());
        room.setPrice(roomDTO.price());
        room.setHotel(hotel);
        return room;
    }
    
    private static String formatMonetaryValue(BigDecimal price) {
        return NumberFormat.getCurrencyInstance().format(price);
    }
}
