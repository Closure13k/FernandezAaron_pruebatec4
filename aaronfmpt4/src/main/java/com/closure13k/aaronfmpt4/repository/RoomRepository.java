package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.availableFrom <= ?1 AND r.availableTo >= ?2 AND r.hotel.city = ?3")
    List<Room> findByDateRangeAndDestination(LocalDate from, LocalDate to, String destination);
    
    @Query("SELECT r FROM Room r WHERE r.hotel.id = ?1 AND r.id = ?2 AND r.isRemoved = false")
    Optional<Room> findByHotelIdAndRoomId(Long hotelId, Long roomId);
    
    @Query("SELECT r FROM Room r WHERE r.hotel.id = ?1 AND r.code = ?2 AND r.isRemoved = false")
    Optional<Room> findByHotelIdAndRoomCode(Long hotelId, String roomCode);
    
    @Query("SELECT r FROM Room r WHERE r.hotel.id = ?1 AND r.hotel.isRemoved = false AND r.isRemoved = false")
    List<Room> findByHotelId(Long id);
}
