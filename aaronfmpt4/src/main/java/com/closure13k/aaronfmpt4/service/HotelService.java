package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.HotelRequestDTO;
import com.closure13k.aaronfmpt4.dto.HotelResponseDTO;
import com.closure13k.aaronfmpt4.dto.RoomRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService implements IHotelService {
    
    @Override
    public List<HotelResponseDTO> getAllHotels() {
        return List.of();
    }
    
    @Override
    public HotelResponseDTO getHotelById(Long id) {
        return null;
    }
    
    @Override
    public HotelResponseDTO createHotel(HotelRequestDTO hotelDTO) {
        return null;
    }
    
    @Override
    public HotelResponseDTO updateHotel(Long id, HotelRequestDTO hotelDTO) {
        return null;
    }
    
    @Override
    public HotelResponseDTO deleteHotel(Long id) {
        return null;
    }
    
    @Override
    public List<RoomResponseDTO> getRoomsByHotelId(Long id) {
        return List.of();
    }
    
    @Override
    public RoomResponseDTO getRoomByHotelIdAndRoomId(Long hotelId, Long roomId) {
        return null;
    }
    
    @Override
    public RoomResponseDTO createRoom(Long id, RoomRequestDTO roomDTO) {
        return null;
    }
    
    @Override
    public RoomResponseDTO updateRoom(Long hotelId, Long roomId, RoomRequestDTO roomDTO) {
        return null;
    }
    
    @Override
    public RoomResponseDTO deleteRoom(Long hotelId, Long roomId) {
        return null;
    }
    
    @Override
    public List<RoomResponseDTO> getRoomsByDateAndDestination(String dateFrom, String dateTo, String destination) {
        return List.of();
    }
}
