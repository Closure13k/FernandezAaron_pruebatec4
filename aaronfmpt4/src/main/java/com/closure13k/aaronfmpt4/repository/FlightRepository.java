package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
