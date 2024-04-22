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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<HotelResponseDTO> getHotelById(
            //? TODO: Add @NotNull and @Positive annotation after implementing the validation.
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getHotelById(id));
    }
    
    
    @PostMapping("/hotels/new")
    public ResponseEntity<HotelResponseDTO> createHotel(
            //? TODO: Add @Valid annotation after implementing the validation.
            @RequestBody HotelRequestDTO hotelDTO) {
        return ResponseEntity.ok(service.createHotel(hotelDTO));
    }
    
    
    @PutMapping("/hotels/edit/{id}")
    public ResponseEntity<HotelResponseDTO> updateHotel(
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long id,
            //? TODO: Add @Valid annotation after implementing the validation.
            @RequestBody HotelRequestDTO hotelDTO) {
        return ResponseEntity.ok(service.updateHotel(id, hotelDTO));
    }
    
    @DeleteMapping("/hotels/delete/{id}")
    public ResponseEntity<HotelResponseDTO> deleteHotel(
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long id) {
        return ResponseEntity.ok(service.deleteHotel(id));
    }
    
    @GetMapping("/hotels/{id}/rooms")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByHotelId(
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long id) {
        return ResponseEntity.ok(service.getRoomsByHotelId(id));
    }
    
    @GetMapping("/hotels/{hotelId}/rooms/{roomId}")
    public ResponseEntity<RoomResponseDTO> getRoomByHotelIdAndRoomId(
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long hotelId,
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long roomId) {
        return ResponseEntity.ok(service.getRoomByHotelIdAndRoomId(hotelId, roomId));
    }
    
    @PostMapping("/hotels/{id}/rooms/new")
    public ResponseEntity<RoomResponseDTO> createRoom(
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long id,
            //? TODO: Add @Valid annotation after implementing the validation.
            @RequestBody RoomRequestDTO roomDTO) {
        return ResponseEntity.ok(service.createRoom(id, roomDTO));
    }
    
    @PutMapping("/hotels/{hotelId}/rooms/edit/{roomId}")
    public ResponseEntity<RoomResponseDTO> updateRoom(
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long hotelId,
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long roomId,
            //? TODO: Add @Valid annotation after implementing the validation.
            @RequestBody RoomRequestDTO roomDTO) {
        return ResponseEntity.ok(service.updateRoom(hotelId, roomId, roomDTO));
    }
    
    @DeleteMapping("/hotels/{hotelId}/rooms/delete/{roomId}")
    public ResponseEntity<RoomResponseDTO> deleteRoom(
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long hotelId,
            //? TODO: Add @NotNull and @Positive annotations after implementing the validation.
            @PathVariable Long roomId) {
        return ResponseEntity.ok(service.deleteRoom(hotelId, roomId));
    }
    
    @GetMapping("/rooms?dateFrom={dateFrom}&dateTo={dateTo}&destination={destination}")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByDateAndDestination(
            //? TODO: Add @NotNull annotation after implementing the validation.
            @RequestParam
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"dd/MM/yyyy", "dd-MM-yyyy"})
            String dateFrom,
            //? TODO: Add @NotNull annotation after implementing the validation.
            @RequestParam
            //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"dd/MM/yyyy", "dd-MM-yyyy"})
            String dateTo,
            //? TODO: Add @NotNull annotation after implementing the validation.
            @RequestParam String destination) {
        return ResponseEntity.ok(service.getRoomsByDateAndDestination(dateFrom, dateTo, destination));
    }
    
    
}

/**
 * TODO: Delete this comment after completing the task.
 * User story 1:
 * * GET /hotels    (Sin devolver lista de habitaciones)
 * User story 2:
 * * GET /rooms?dateFrom=dd/mm/aaaa&dateTo=dd/mm/aaaa&destination="nombre_destino"
 * User story 3:
 * * POST /room-booking/new
 * Y el CRUD de hoteles y habitaciones:
 * * GET /hotels/{id}               (lectura)
 * * POST /hotels/new               (creación)
 * * PUT /hotels/edit/{id}          (actualización)
 * * DELETE /hotels/delete/{id}     (borrado lógico)
 * * GET /hotels/{id}/rooms         (para obtener todas las habitaciones de un hotel)
 * * GET /hotels/{id}/rooms/{id}    (para obtener una habitación de un hotel)
 * * POST /hotels/{id}/rooms/new    (para añadir una habitación a un hotel)
 * * PUT /hotels/{id}/rooms/edit/{id} (actualización)
 * * DELETE /hotels/{id}/rooms/delete/{id} (borrado lógico)
 */