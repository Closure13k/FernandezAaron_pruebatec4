package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    @Query("SELECT f FROM Flight f WHERE f.isRemoved = false")
    List<Flight> findAllActive();
    
    @Query("SELECT f FROM Flight f WHERE f.id = :id AND f.isRemoved = false")
    Optional<Flight> findActiveById(Long id);
    
    Optional<Flight> findByCode(String code);
}
