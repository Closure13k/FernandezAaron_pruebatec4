package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Integer> {
}
