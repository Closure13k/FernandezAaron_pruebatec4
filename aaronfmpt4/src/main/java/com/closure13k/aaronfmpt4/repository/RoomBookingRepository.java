package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Integer> {
}
