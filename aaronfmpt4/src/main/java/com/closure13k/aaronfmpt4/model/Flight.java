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
    
    @Column(unique = true, nullable = false, length = 50)
    private String code;
    
    @Column(nullable = false, length = 100)
    private String origin;
    
    @Column(nullable = false, length = 100)
    private String destination;
    
    @Column(nullable = false)
    private LocalDate departureDate;
    
    @Column(nullable = false)
    private Integer availableSeats;
    
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;
    
    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
    private List<FlightBooking> bookingList;
    
    private Boolean isRemoved = false;
    
    /**
     * Generate a code for the flight if one is not provided.
     * Generates the code from the first 3 letters of the origin and destination.
     * Then adds the number of available seats, the departure date, and the current day of the year.
     */
    public void generateCode() {
        this.code = origin.substring(0, 3).toUpperCase()
                + destination.substring(0, 3).toUpperCase()
                + String.format("%04d", availableSeats)
                + departureDate.toString().replace("-", "")
                + LocalDate.now().getDayOfYear();
    }
}