package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.RoomBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.RoomBookingResponseDTO;

import java.util.List;

public interface IRoomBookingService {
    
    List<RoomBookingResponseDTO> getAllRoomBookings();
    
    RoomBookingResponseDTO getRoomBookingById(Long id);
    
    RoomBookingResponseDTO createRoomBooking(RoomBookingRequestDTO roomBookingDTO);
    
    void updateRoomBooking(Long id, RoomBookingRequestDTO roomBookingDTO);
    
    void deleteRoomBooking(Long id);
}
