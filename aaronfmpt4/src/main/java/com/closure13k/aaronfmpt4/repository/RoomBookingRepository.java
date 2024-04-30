package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    
    @Query("SELECT EXISTS (FROM RoomBooking b WHERE b.room.id = :roomId AND (b.id != :bookingId OR :bookingId IS NULL) AND ((b.startDate < :endDate) AND (b.endDate > :startDate)))")
    boolean isRoomBooked(Long roomId, Long bookingId, LocalDate startDate, LocalDate endDate);
}
