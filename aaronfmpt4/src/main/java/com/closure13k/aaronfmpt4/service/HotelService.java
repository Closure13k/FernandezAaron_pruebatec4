package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.request.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.RoomResponseDTO;
import com.closure13k.aaronfmpt4.exception.DTOValidationException;
import com.closure13k.aaronfmpt4.exception.EntityNotFoundException;
import com.closure13k.aaronfmpt4.exception.ExistingEntityException;
import com.closure13k.aaronfmpt4.model.Hotel;
import com.closure13k.aaronfmpt4.model.Room;
import com.closure13k.aaronfmpt4.model.RoomBooking;
import com.closure13k.aaronfmpt4.repository.HotelRepository;
import com.closure13k.aaronfmpt4.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

/**
 * Service class that handles the business logic for hotels and rooms.
 */
@RequiredArgsConstructor
@Service
public class HotelService implements IHotelService {
    
    public static final String HOTEL_ENTITY = "Hotel";
    public static final String ROOM_ENTITY = "Room";
    
    private final HotelRepository hotelRepository;
    
    private final RoomRepository roomRepository;
    
    /**
     * Returns a list of all hotels.
     *
     * @return A list of HotelResponseDTOs.
     * @throws EntityNotFoundException if no hotels exist.
     */
    @Override
    public List<HotelResponseDTO> getAllHotels() {
        List<Hotel> allActiveHotels = hotelRepository.findAllActive();
        if (allActiveHotels.isEmpty()) {
            throw EntityNotFoundException.listIsEmpty(HOTEL_ENTITY);
        }
        
        return allActiveHotels.stream()
                .map(HotelService::toBasicHotelDTO)
                .toList();
    }
    
    /**
     * Returns a hotel by its id.
     *
     * @param id The id of the hotel.
     * @return The HotelResponseDTO of the hotel.
     * @throws EntityNotFoundException if the hotel is not found.
     */
    @Override
    public HotelResponseDTO getHotelById(Long id) {
        return hotelRepository.findActiveById(id)
                .map(HotelService::toBasicHotelDTO)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
    }
    
    /**
     * Creates a hotel. If no code is provided in the request, a code will be generated from the name and city.
     *
     * @param hotelDTO The HotelRequestDTO with the fields to create the hotel.
     * @return The HotelResponseDTO of the created hotel.
     * @throws ExistingEntityException if a hotel with the same code already exists.
     * @see #fromDTO(HotelRequestDTO) fromDTO for the conversion logic.
     * @see #toBasicHotelDTOWithId(Hotel) toBasicHotelDTOWithId for the conversion logic.
     * @see #createCodeIfNotExists(Hotel) createCodeIfNotExists for additional logic.
     */
    @Override
    public HotelResponseDTO createHotel(HotelRequestDTO hotelDTO) {
        hotelRepository.findByCode(hotelDTO.code())
                .ifPresent(hotel -> {
                    throw ExistingEntityException.byUniqueField(HOTEL_ENTITY, "code", hotelDTO.code());
                });
        
        Hotel saved = hotelRepository.save(createCodeIfNotExists(this.fromDTO(hotelDTO)));
        
        return toBasicHotelDTOWithId(saved);
    }
    
    /**
     * Creates a list of hotels.
     *
     * @param hotelDTOs The list of HotelRequestDTOs with the fields to create the hotels.
     * @return A list of HotelResponseDTOs of the created hotels.
     */
    @Override
    public List<HotelResponseDTO> createHotelsFromList(List<HotelRequestDTO> hotelDTOs) {
        
        List<Hotel> hotels = hotelRepository.saveAll(
                hotelDTOs.stream()
                        .map(hotelDTO -> createCodeIfNotExists(this.fromDTO(hotelDTO)))
                        .toList()
        );
        
        return hotels.stream()
                .map(HotelService::toBasicHotelDTOWithId)
                .toList();
    }
    
    /**
     * Updates a hotel.
     *
     * @param id       The id of the hotel.
     * @param hotelDTO The HotelRequestDTO with the fields to update.
     * @throws EntityNotFoundException if the hotel is not found.
     * @throws DTOValidationException  if no fields were updated.
     * @see #checkAndApplyUpdates(HotelRequestDTO, Hotel) checkAndApplyUpdates for the update logic.
     */
    @Override
    public void updateHotel(Long id, HotelRequestDTO hotelDTO) {
        Hotel hotel = hotelRepository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
        
        
        if (checkAndApplyUpdates(hotelDTO, hotel) == 0) {
            throw DTOValidationException.noFieldsToUpdate();
        }
        
        hotelRepository.save(hotel);
    }
    
    /**
     * Deletes a hotel.
     *
     * @param id The id of the hotel.
     * @throws EntityNotFoundException if the hotel is not found.
     * @throws ExistingEntityException if the hotel has active rooms.
     */
    @Override
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
        
        if (hasActiveChildren(hotel)) {
            throw ExistingEntityException.hasActiveChildren(HOTEL_ENTITY, ROOM_ENTITY, id);
        }
        
        hotel.setIsRemoved(true);
        hotelRepository.save(hotel);
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
                .orElseThrow(() -> EntityNotFoundException.byId(ROOM_ENTITY, roomId));
    }
    
    /**
     * Creates a room in the hotel.
     * If no code is provided in the request, a code will be generated from the hotel code and room type,
     * adding a 2-digit number from the count of room codes starting with the same hotel code and room type.
     *
     * @param id      The id of the hotel.
     * @param roomDTO The RoomRequestDTO with the fields to create the room.
     * @return The RoomResponseDTO of the created room.
     * @throws EntityNotFoundException if the hotel is not found.
     * @see #fromDTO(RoomRequestDTO, Hotel) fromDTO for the conversion logic.
     * @see #toBasicRoomDTO(Room) toBasicRoomDTO for the conversion logic.
     */
    @Override
    public RoomResponseDTO createRoom(Long id, RoomRequestDTO roomDTO) {
        Hotel hotel = hotelRepository.findActiveById(id)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, id));
        
        Room room = fromDTO(roomDTO, hotel);
        if (room.getCode() == null || room.getCode().isBlank()) {
            room.generateCode(roomRepository
                    .countByCodeStartingWith(
                            hotel.getCode() + room.getType().substring(0, 1).toUpperCase()) + 1);
        }
        roomRepository.save(room);
        
        return toBasicRoomDTO(room);
    }
    
    /**
     * Updates a room in the hotel.
     *
     * @param hotelId The id of the hotel.
     * @param roomId  The id of the room.
     * @param roomDTO The RoomRequestDTO with the fields to update.
     * @throws EntityNotFoundException if the hotel or room is not found.
     * @throws DTOValidationException  if no fields were updated.
     * @see #checkAndApplyUpdates(RoomRequestDTO, Room) checkAndApplyUpdates for the update logic.
     */
    @Override
    public void updateRoom(Long hotelId, Long roomId, RoomRequestDTO roomDTO) {
        Hotel hotel = hotelRepository.findActiveById(hotelId)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, hotelId));
        Room room = roomRepository.findByHotelIdAndRoomId(hotel.getId(), roomId)
                .orElseThrow(() -> EntityNotFoundException.byId(ROOM_ENTITY, roomId));
        
        int updates = checkAndApplyUpdates(roomDTO, room);
        if (updates == 0) {
            throw DTOValidationException.noFieldsToUpdate();
        }
        
        roomRepository.save(room);
    }
    
    /**
     * Deletes a room from the hotel.
     *
     * @param hotelId The id of the hotel.
     * @param roomId  The id of the room.
     * @return The RoomResponseDTO of the deleted room.
     * @throws EntityNotFoundException if the hotel or room is not found.
     */
    @Override
    public RoomResponseDTO deleteRoom(Long hotelId, Long roomId) {
        Hotel hotel = hotelRepository.findActiveById(hotelId)
                .orElseThrow(() -> EntityNotFoundException.byId(HOTEL_ENTITY, hotelId));
        
        Room room = roomRepository.findByHotelIdAndRoomId(hotel.getId(), roomId)
                .orElseThrow(() -> EntityNotFoundException.byId(ROOM_ENTITY, roomId));
        
        //Comprobar que la habitación no tenga reservas activas actualmente.
        if (checkIfHasActiveBookings(room)) {
            throw ExistingEntityException.hasActiveChildren(ROOM_ENTITY, "Room Booking", roomId);
        }
        
        room.setIsRemoved(true);
        roomRepository.save(room);
        
        return toBasicRoomDTO(room);
    }
    
    /**
     * Checks if a room has active bookings before deletion.
     * If the room has active bookings, an ExistingEntityException will be thrown.
     *
     * @param room The room to check.
     * @return True if the room has active bookings, false otherwise.
     */
    private boolean checkIfHasActiveBookings(Room room) {
        return room.getBookingList().stream()
                .anyMatch(hasActiveBookingAsOf(LocalDate.now()));
    }
    
    /**
     * Returns a predicate that checks if a room booking is active as of the given date.
     * The booking is active if the check-in date is before or equal to the given date
     * and the check-out date is after or equal to the given date.
     *
     * @param date The date to check.
     * @return The predicate.
     */
    private static Predicate<RoomBooking> hasActiveBookingAsOf(LocalDate date) {
        return booking -> {
            LocalDate checkIn = booking.getStartDate();
            LocalDate checkOut = booking.getEndDate();
            return (checkIn.isBefore(date) || checkIn.isEqual(date)) &&
                    (checkOut.isAfter(date) || checkOut.isEqual(date));
            
        };
    }
    
    /**
     * Returns a list of rooms that are available between the given dates and are located in the given destination.
     *
     * @param startDate   The start date of the date range.
     * @param endDate     The end date of the date range.
     * @param destination The destination of the rooms.
     * @return A list of RoomResponseDTOs.
     * @throws EntityNotFoundException if no rooms are found.
     */
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
    
    /**
     * Checks the fields in the HotelRequestDTO and applies the updates to the Hotel entity,
     * then returns the number of fields that were updated.
     * <p>
     * If no fields were updated, a DTOValidationException should be thrown by the caller.
     * <p>
     * If the 'code' field is provided, it must be unique, otherwise an ExistingEntityException will be thrown.
     *
     * @param hotelDTO The HotelRequestDTO with the fields to update.
     * @param hotel    The Hotel entity to update.
     * @return The number of fields that were updated.
     */
    private int checkAndApplyUpdates(HotelRequestDTO hotelDTO, Hotel hotel) {
        int updates = 0;
        String code = hotelDTO.code();
        if (code != null && !code.isBlank() && !code.equals(hotel.getCode())) {
            hotelRepository.findByCode(code)
                    .ifPresent(h -> {
                        throw ExistingEntityException.byUniqueField(HOTEL_ENTITY, "code", code);
                    });
            hotel.setCode(code);
            updates++;
        }
        
        String name = hotelDTO.name();
        if (name != null && !name.isBlank()) {
            hotel.setName(name);
            updates++;
        }
        
        String city = hotelDTO.city();
        if (city != null && !city.isBlank()) {
            hotel.setCity(city);
            updates++;
        }
        return updates;
    }
    
    private static boolean hasActiveChildren(Hotel hotel) {
        return hotel.getRooms().stream().anyMatch(room -> !room.getIsRemoved());
    }
    
    /**
     * Checks the fields in the RoomRequestDTO and applies the updates to the Room entity,
     * then returns the number of fields that were updated.
     * <p>
     * If no fields were updated, a DTOValidationException should be thrown by the caller.
     * <p>
     * If dates are provided, both 'availableFrom' and 'availableTo' must be provided and
     * 'availableFrom' must be before 'availableTo', otherwise a DTOValidationException will be thrown.
     *
     * @param roomDTO The RoomRequestDTO with the fields to update.
     * @param room    The Room entity to update.
     * @return The number of fields that were updated.
     * @throws DTOValidationException if the provided dates do not pass validation.
     */
    private int checkAndApplyUpdates(RoomRequestDTO roomDTO, Room room) {
        int updates = 0;
        String type = roomDTO.type();
        if (type != null && !type.isBlank()) {
            room.setType(type);
            updates++;
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
            updates++;
        }
        
        BigDecimal price = roomDTO.price();
        if (price != null) {
            room.setPrice(price);
            updates++;
        }
        
        return updates;
    }
    
    /**
     * Generates a code for the hotel if it was not provided in the request.
     *
     * @param hotel The hotel entity from which to generate the code.
     * @return The hotel entity with the generated code.
     */
    private Hotel createCodeIfNotExists(Hotel hotel) {
        if (hotel.getCode() == null || hotel.getCode().isBlank()) {
            String codeCharacters = generateCodeCharacters(hotel.getName()) + generateCodeCharacters(hotel.getCity()).toUpperCase();
            Integer codeCount = hotelRepository.countByCodeStartingWith(codeCharacters) + 1;
            hotel.generateCode(codeCharacters, codeCount);
        }
        return hotel;
    }
    
    /**
     * Generates a code from the first two characters of the first two words of the input.
     * If the input has only one word, the code will be the first two characters of the word.
     * If the input has two words, the code will be the first character of each word.
     *
     * @param input The input string.
     * @return The generated code.
     */
    private String generateCodeCharacters(String input) {
        final String regex = "[ \\-_]";
        String[] splitInput = input.split(regex);
        String codeCharacters;
        if (splitInput.length < 2) { //Pick the first two characters of the input
            codeCharacters = splitInput[0].substring(0, 2);
        } else { //Pick the first character of the first word and the first character of the second word
            codeCharacters = splitInput[0].substring(0, 1) + splitInput[1].substring(0, 1);
        }
        return codeCharacters.toUpperCase();
    }
    
    /**
     * Converts a HotelRequestDTO to a Hotel entity.
     *
     * @param request The HotelRequestDTO to convert.
     * @return The Hotel entity.
     */
    private Hotel fromDTO(HotelRequestDTO request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.name());
        hotel.setCity(request.city());
        hotel.setCode(request.code());
        hotel.setIsRemoved(false);
        return hotel;
    }
    
    /**
     * Converts a RoomRequestDTO to a Room entity.
     *
     * @param roomDTO The RoomRequestDTO to convert.
     * @param hotel   The Hotel entity to which the room belongs.
     * @return The Room entity.
     */
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
    
    /**
     * Converts a Hotel entity to a HotelResponseDTO.
     * Returns the HotelResponseDTO with the following fields:
     * id, code, name, city.
     *
     * @param hotel The Hotel entity to convert.
     * @return The HotelResponseDTO.
     */
    private static HotelResponseDTO toBasicHotelDTOWithId(Hotel hotel) {
        return HotelResponseDTO.builder()
                .withId(hotel.getId())
                .withCode(hotel.getCode())
                .withName(hotel.getName())
                .withCity(hotel.getCity())
                .build();
    }
    
    /**
     * Converts a Hotel entity to a HotelResponseDTO.
     * Returns the HotelResponseDTO with the following fields:
     * code, name, city.
     *
     * @param hotel The Hotel entity to convert.
     * @return The HotelResponseDTO.
     */
    private static HotelResponseDTO toBasicHotelDTO(Hotel hotel) {
        return HotelResponseDTO.builder()
                .withCode(hotel.getCode())
                .withName(hotel.getName())
                .withCity(hotel.getCity())
                .build();
    }
    
    /**
     * Converts a Room entity to a RoomResponseDTO.
     * Returns the RoomResponseDTO with the following fields:
     * code, type, availableFrom, availableTo, price.
     *
     * @param room The Room entity to convert.
     * @return The RoomResponseDTO.
     */
    private static RoomResponseDTO toBasicRoomDTO(Room room) {
        return RoomResponseDTO.builder()
                .withCode(room.getCode())
                .withType(room.getType())
                .withAvailableFrom(room.getAvailableFrom().toString())
                .withAvailableTo(room.getAvailableTo().toString())
                .withPrice(room.getPrice())
                .build();
    }
}
