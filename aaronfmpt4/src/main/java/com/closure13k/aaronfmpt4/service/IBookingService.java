package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.RoomBookingRequestDTO;
import com.closure13k.aaronfmpt4.dto.RoomBookingResponseDTO;
import org.springframework.stereotype.Service;

public interface IBookingService {
    RoomBookingResponseDTO createRoomBooking(RoomBookingRequestDTO roomBookingDTO);
    
}
