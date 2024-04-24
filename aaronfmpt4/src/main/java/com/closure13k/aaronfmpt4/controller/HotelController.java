package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomResponseDTO;
import com.closure13k.aaronfmpt4.service.IHotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<HotelResponseDTO> getHotelById(@NotNull @Positive @PathVariable Long id) {
        return ResponseEntity.ok(service.getHotelById(id));
    }
    
    
    @PostMapping("/hotels/new")
    public ResponseEntity<HotelResponseDTO> createHotel(
            @Valid
            @RequestBody HotelRequestDTO hotelDTO) {
        return ResponseEntity.ok(service.createHotel(hotelDTO));
    }
    
    @PostMapping("/hotels/newbatch")
    public ResponseEntity<List<HotelResponseDTO>> createHotels(@RequestBody List<HotelRequestDTO> hotelDTOs) {
        return ResponseEntity.ok(service.createHotelsFromList(hotelDTOs));
    }
    
    
    @PutMapping("/hotels/edit/{id}")
    public ResponseEntity<HotelResponseDTO> updateHotel(@NotNull @Positive @PathVariable Long id,
                                                        @Valid @RequestBody HotelRequestDTO hotelDTO) {
        service.updateHotel(id, hotelDTO);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/hotels/delete/{id}")
    public ResponseEntity<HotelResponseDTO> deleteHotel(@NotNull @Positive @PathVariable Long id) {
        service.deleteHotel(id);
        return ResponseEntity.accepted().build();
    }
    
    @GetMapping("/hotels/{id}/rooms")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByHotelId(@NotNull @Positive @PathVariable Long id) {
        return ResponseEntity.ok(service.getRoomsByHotelId(id));
    }
    
    @GetMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<RoomResponseDTO> getRoomByHotelIdAndRoomId(@NotNull @Positive @PathVariable Long hotelId,
                                                                     @NotNull @Positive @PathVariable Long roomId) {
        return ResponseEntity.ok(service.getRoomByHotelIdAndRoomId(hotelId, roomId));
    }
    
    @PostMapping("/hotels/{id}/rooms/new")
    public ResponseEntity<RoomResponseDTO> createRoom(@NotNull @Positive @PathVariable Long id,
                                                      @Valid @RequestBody RoomRequestDTO roomDTO) {
        return ResponseEntity.ok(service.createRoom(id, roomDTO));
    }
    
    @PutMapping("/hotels/{hotelId}/rooms/edit/{roomId}")
    public ResponseEntity<RoomResponseDTO> updateRoom(@NotNull @Positive @PathVariable Long hotelId,
                                                      @NotNull @Positive @PathVariable Long roomId,
                                                      @Valid @RequestBody RoomRequestDTO roomDTO) {
        service.updateRoom(hotelId, roomId, roomDTO);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/hotels/{hotelId}/rooms/delete/{roomId}")
    public ResponseEntity<RoomResponseDTO> deleteRoom(@NotNull @Positive @PathVariable Long hotelId,
                                                      @NotNull @Positive @PathVariable Long roomId) {
        return ResponseEntity.ok(service.deleteRoom(hotelId, roomId));
    }
    
    @GetMapping("/hotels/rooms")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByDateAndDestination(
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE,
                    fallbackPatterns = {"yyyy/MM/dd", "dd-MM-yyyy", "dd/MM/yyyy"}
            )
            @RequestParam LocalDate dateFrom,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate dateTo,
            @RequestParam String destination) {
        return ResponseEntity.ok(service.getRoomsByDateAndDestination(dateFrom, dateTo, destination));
    }
    
    


    
}