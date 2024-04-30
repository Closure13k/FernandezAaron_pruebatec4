package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.request.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.request.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.RoomResponseDTO;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnCreate;
import com.closure13k.aaronfmpt4.dto.validationgroup.OnUpdate;
import com.closure13k.aaronfmpt4.service.IHotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for the Hotel and Room entities.
 * <p>
 * This class is responsible for handling the HTTP requests related to the Hotel and Room entities.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/agency")
public class HotelController {
    private final IHotelService service;
    
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelResponseDTO>> getHotels() {
        return ResponseEntity.ok(service.getAllHotels());
    }
    
    
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelResponseDTO> getHotelById(@Positive @PathVariable Long id) {
        return ResponseEntity.ok(service.getHotelById(id));
    }
    
    
    @PostMapping("/hotels/new")
    public ResponseEntity<HotelResponseDTO> createHotel(@Validated(OnCreate.class) @RequestBody HotelRequestDTO hotel) {
        return ResponseEntity.ok(service.createHotel(hotel));
    }
    
    @PostMapping("/hotels/newbatch")
    public ResponseEntity<List<HotelResponseDTO>> createHotels(@RequestBody List<HotelRequestDTO> hotels) {
        return ResponseEntity.ok(service.createHotelsFromList(hotels));
    }
    
    
    @PutMapping("/hotels/edit/{id}")
    public ResponseEntity<HotelResponseDTO> updateHotel(@Positive @PathVariable Long id,
                                                        @Validated(OnUpdate.class) @RequestBody HotelRequestDTO hotel) {
        service.updateHotel(id, hotel);
        return ResponseEntity.ok(service.getHotelById(id));
    }
    
    @DeleteMapping("/hotels/delete/{id}")
    public ResponseEntity<HotelResponseDTO> deleteHotel(@Positive @PathVariable Long id) {
        service.deleteHotel(id);
        return ResponseEntity.accepted().build();
    }
    
    @GetMapping("/hotels/{id}/rooms")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByHotelId(@NotNull @Positive @PathVariable Long id) {
        return ResponseEntity.ok(service.getRoomsByHotelId(id));
    }
    
    @GetMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<RoomResponseDTO> getRoomByHotelIdAndRoomId(@Positive @PathVariable Long hotelId,
                                                                     @Positive @PathVariable Long roomId) {
        return ResponseEntity.ok(service.getRoomByHotelIdAndRoomId(hotelId, roomId));
    }
    
    @PostMapping("/hotels/{id}/rooms/new")
    public ResponseEntity<RoomResponseDTO> createRoom(@Positive @PathVariable Long id,
                                                      @Validated(OnCreate.class) @RequestBody RoomRequestDTO room) {
        return ResponseEntity.ok(service.createRoom(id, room));
    }
    
    @PutMapping("/hotels/{hotelId}/rooms/edit/{roomId}")
    public ResponseEntity<RoomResponseDTO> updateRoom(@Positive @PathVariable Long hotelId,
                                                      @Positive @PathVariable Long roomId,
                                                      @Validated(OnUpdate.class) @RequestBody RoomRequestDTO room) {
        service.updateRoom(hotelId, roomId, room);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/hotels/{hotelId}/rooms/delete/{roomId}")
    public ResponseEntity<RoomResponseDTO> deleteRoom(@Positive @PathVariable Long hotelId,
                                                      @Positive @PathVariable Long roomId) {
        return ResponseEntity.ok(service.deleteRoom(hotelId, roomId));
    }
    
    @GetMapping("/hotels/rooms")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByDateAndDestination(
            @DateTimeFormat(fallbackPatterns = {"yyyy/MM/dd", "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd"})
            @RequestParam LocalDate dateFrom,
            @DateTimeFormat(fallbackPatterns = {"yyyy/MM/dd", "dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd"})
            @RequestParam LocalDate dateTo,
            @RequestParam String destination) {
        return ResponseEntity.ok(service.getRoomsByDateAndDestination(dateFrom, dateTo, destination));
    }
}