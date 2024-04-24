package com.closure13k.aaronfmpt4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 20)
    private String code; // ? TODO: Implementar algoritmo para generar código de vuelo en base a origen, destino y fecha.
    
    @Column(nullable = false, length = 100)
    private String origin;
    
    @Column(nullable = false, length = 100)
    private String destination;
    
    @Column(nullable = false)
    private LocalDate departureDate;
    
    @Column(nullable = false)
    private String seatType;
    
    @Column(nullable = false)
    private Integer availableSeats;
    
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;
    
    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
    private List<FlightBooking> bookingList;
    
    private Boolean isRemoved = false;
    
}
