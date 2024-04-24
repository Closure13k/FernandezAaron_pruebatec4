package com.closure13k.aaronfmpt4.controller;

import com.closure13k.aaronfmpt4.dto.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomResponseDTO;
import com.closure13k.aaronfmpt4.exception.ExistingEntityException;
import com.closure13k.aaronfmpt4.exception.HotelNotFoundException;
import com.closure13k.aaronfmpt4.exception.RoomNotFoundException;
import com.closure13k.aaronfmpt4.service.IHotelService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                                                        @RequestBody HotelRequestDTO hotelDTO) {
        service.updateHotel(id, hotelDTO);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/hotels/delete/{id}")
    public ResponseEntity<HotelResponseDTO> deleteHotel(@NotNull @Positive @PathVariable Long id) {
        service.deleteHotel(id);
        return ResponseEntity.ok().build();
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
                                                      @RequestBody RoomRequestDTO roomDTO) {
        service.updateRoom(hotelId, roomId, roomDTO);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/hotels/{hotelId}/rooms/delete/{roomId}")
    public ResponseEntity<RoomResponseDTO> deleteRoom(@NotNull @Positive @PathVariable Long hotelId,
                                                      @NotNull @Positive @PathVariable Long roomId) {
        return ResponseEntity.ok(service.deleteRoom(hotelId, roomId));
    }
    
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponseDTO>> getRoomsByDateAndDestination(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate dateFrom,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate dateTo,
            @NotBlank @RequestParam String destination) {
        return ResponseEntity.ok(service.getRoomsByDateAndDestination(dateFrom, dateTo, destination));
    }
    
    
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    
    @ExceptionHandler({HotelNotFoundException.class, RoomNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFoundException(Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    
    @ExceptionHandler(ExistingEntityException.class)
    public ResponseEntity<Map<String, String>> handleExistingEntityException(ExistingEntityException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }
    
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, List<String>>> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        Map<String, List<String>> response = new HashMap<>();
        List<String> errors = e.getAllErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();
        
        response.put("errors", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
}