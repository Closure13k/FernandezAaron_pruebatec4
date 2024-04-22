package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IHotelService {
    List<HotelResponseDTO> getAllHotels();
    
    HotelResponseDTO getHotelById(Long id);
    
    HotelResponseDTO createHotel(HotelRequestDTO hotelDTO);
    
    HotelResponseDTO updateHotel(Long id, HotelRequestDTO hotelDTO);
    
    HotelResponseDTO deleteHotel(Long id);
    
    List<RoomResponseDTO> getRoomsByHotelId(Long id);
    
    RoomResponseDTO getRoomByHotelIdAndRoomId(Long hotelId, Long roomId);
    
    RoomResponseDTO createRoom(Long id, RoomRequestDTO roomDTO);
    
    RoomResponseDTO updateRoom(Long hotelId, Long roomId, RoomRequestDTO roomDTO);
    
    RoomResponseDTO deleteRoom(Long hotelId, Long roomId);
    
    List<RoomResponseDTO> getRoomsByDateAndDestination(String dateFrom, String dateTo, String destination);
}
