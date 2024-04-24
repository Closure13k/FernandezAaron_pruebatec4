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
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 10)
    private String code;
    
    @Column(nullable = false, length = 50)
    private String type;
    
    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private LocalDate availableFrom;
    
    @Column(nullable = false)
    private LocalDate availableTo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomBooking> bookingList;
    
    private Boolean isRemoved = false;
    
}
