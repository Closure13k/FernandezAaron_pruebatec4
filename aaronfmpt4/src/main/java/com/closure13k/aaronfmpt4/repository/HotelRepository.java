package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    
    @Query("FROM Hotel h WHERE h.isRemoved = false")
    List<Hotel> findAllActive();
    
    @Query("FROM Hotel h WHERE h.id = :id AND h.isRemoved = false")
    Optional<Hotel> findActiveById(Long id);
    
    Integer countByCodeStartingWith(String code);
    
    Optional<Hotel> findByCode(String code);
    
}
