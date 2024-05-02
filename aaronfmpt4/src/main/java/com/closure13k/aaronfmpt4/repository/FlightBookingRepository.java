package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.Client;
import com.closure13k.aaronfmpt4.model.Flight;
import com.closure13k.aaronfmpt4.model.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
    @Query("SELECT fb FROM FlightBooking fb WHERE fb.client = :client AND fb.flight = :flight")
    Optional<FlightBooking> findByClientAndFlight(Client client, Flight flight);
}
