package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomResponseDTO;
import com.closure13k.aaronfmpt4.exception.DTOValidationException;
import com.closure13k.aaronfmpt4.exception.EntityNotFoundException;
import com.closure13k.aaronfmpt4.exception.ExistingEntityException;
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
    
    public static final String HOTEL_ENTITY = "Hotel";
    private final HotelRepository hotelRepository;
    
    private final RoomRepository roomRepository;
    
    @Override
    public List<HotelResponseDTO> getAllHotels() {
        List<Hotel> allActiveHotels = hotelRepository.findAllActive();
        if (allActiveHotels.isEmpty()) {
            throw EntityNotFoundException.listIsEmpty("Hotels");
        }
        
        return allActiveHotels.stream()
                .map(HotelService::toBasicHotelDTO)
                .toList();
    }
    
    
    @Override
    public HotelResponseDTO getHotelById(Long id) {
        return hotelRepository.findActiveById(id)
                .map(HotelService::toBasicHotelDTO)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
    }
    
    
    @Override
    public HotelResponseDTO createHotel(HotelRequestDTO hotelDTO) {
        hotelRepository.findByCode(hotelDTO.code())
                .ifPresent(hotel -> {
                    throw ExistingEntityException.byUniqueField(HOTEL_ENTITY, "code", hotelDTO.code());
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
        Hotel hotel = hotelRepository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
        
        
        String code = hotelDTO.code();
        if (code != null && !code.isBlank() && !code.equals(hotel.getCode())) {
            hotelRepository.findByCode(code)
                    .ifPresent(h -> {
                        throw ExistingEntityException.byUniqueField(HOTEL_ENTITY, "code", code);
                    });
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
        Hotel hotel = hotelRepository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
        if (Boolean.TRUE.equals(hotel.getIsRemoved())) {
            throw EntityNotFoundException.byId(HOTEL_ENTITY, id);
        }
        
        if (hasActiveChildren(hotel)) {
            throw ExistingEntityException.hasActiveChildren(HOTEL_ENTITY, "rooms", id);
        }
        
        hotel.setIsRemoved(true);
        hotelRepository.save(hotel);
    }
    
    private static boolean hasActiveChildren(Hotel hotel) {
        return hotel.getRooms().stream().anyMatch(room -> !room.getIsRemoved());
    }
    
    @Override
    public List<RoomResponseDTO> getRoomsByHotelId(Long id) {
        Hotel hotel = hotelRepository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
        List<Room> rooms = roomRepository.findByActiveHotelId(hotel.getId());
        if (rooms.isEmpty()) {
            throw EntityNotFoundException.listIsEmpty("Rooms");
        }
        
        return rooms.stream()
                .map(HotelService::toBasicRoomDTO)
                .toList();
    }
    
    
    @Override
    public RoomResponseDTO getRoomByHotelIdAndRoomId(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findActiveById(hotelId)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, hotelId));
        
        return hotel.getRooms().stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst()
                .map(HotelService::toBasicRoomDTO)
                .orElseThrow(() -> EntityNotFoundException.byId("Room", roomId));
    }
    
    @Override
    public RoomResponseDTO createRoom(Long id, RoomRequestDTO roomDTO) {
        Hotel hotel = hotelRepository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
        
        Room room = fromDTO(roomDTO, hotel);
        roomRepository.save(room);
        
        return toBasicRoomDTO(room);
    }
    
    
    // * TODO: Review all this logic
    @Override
    public void updateRoom(Long hotelId, Long roomId, RoomRequestDTO roomDTO) {
        Hotel hotel = hotelRepository.findActiveById(hotelId)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, hotelId));
        Room room = roomRepository.findByHotelIdAndRoomId(hotel.getId(), roomId)
                .orElseThrow(() -> EntityNotFoundException.byId("Room", roomId));
        
        String type = roomDTO.type();
        if (type != null && !type.isBlank()) {
            room.setType(type);
        }
        
        LocalDate availableFrom = roomDTO.availableFrom();
        if (availableFrom != null) {
            LocalDate availableTo = roomDTO.availableTo();
            if (availableTo == null) {
                throw new DTOValidationException("'availableTo' cannot be null if 'availableFrom' is provided");
            }
            if (availableFrom.isAfter(availableTo)) {
                throw new DTOValidationException("availableFrom cannot be after availableTo");
            }
            room.setAvailableFrom(availableFrom);
            room.setAvailableTo(availableTo);
        }
        
        BigDecimal price = roomDTO.price();
        if (price != null) {
            room.setPrice(price);
        }
        
        roomRepository.save(room);
    }
    
    @Override
    public RoomResponseDTO deleteRoom(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findActiveById(hotelId)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, hotelId));
        
        Room room = roomRepository.findByHotelIdAndRoomId(hotel.getId(), roomId)
                .orElseThrow(() -> EntityNotFoundException.byId("Room", roomId));
        
        room.setIsRemoved(true);
        roomRepository.save(room);
        
        return toBasicRoomDTO(room);
    }
    
    @Override
    public List<RoomResponseDTO> getRoomsByDateAndDestination(LocalDate startDate, LocalDate endDate, String destination) {
        List<Room> rooms = roomRepository
                .findByDateRangeAndDestination(startDate, endDate, destination);
        if (rooms.isEmpty()) {
            throw EntityNotFoundException.listIsEmpty("Rooms");
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
                .withPrice(NumberFormat.getCurrencyInstance().format(room.getPrice()))
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
}
