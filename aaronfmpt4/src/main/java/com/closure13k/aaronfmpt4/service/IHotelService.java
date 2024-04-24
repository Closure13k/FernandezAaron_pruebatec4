package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomResponseDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface IHotelService {
    List<HotelResponseDTO> getAllHotels();
    
    HotelResponseDTO getHotelById(Long id);
    
    HotelResponseDTO createHotel(HotelRequestDTO hotelDTO);
    
    void updateHotel(Long id, HotelRequestDTO hotelDTO);
    
    void deleteHotel(Long id);
    
    List<RoomResponseDTO> getRoomsByHotelId(Long id);
    
    RoomResponseDTO getRoomByHotelIdAndRoomId(Long hotelId, Long roomId);
    
    RoomResponseDTO createRoom(Long id, RoomRequestDTO roomDTO);
    
    void updateRoom(Long hotelId, Long roomId, RoomRequestDTO roomDTO);
    
    RoomResponseDTO deleteRoom(Long hotelId, Long roomId);
    
    List<RoomResponseDTO> getRoomsByDateAndDestination(LocalDate dateFrom, LocalDate dateTo, String destination);
    
    List<HotelResponseDTO> createHotelsFromList(List<HotelRequestDTO> hotelDTOs);
}
