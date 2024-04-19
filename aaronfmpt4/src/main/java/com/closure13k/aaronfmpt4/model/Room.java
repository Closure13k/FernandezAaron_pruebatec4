package com.closure13k.aaronfmpt4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "HABITACION")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "codigo")
    private String type;
    @Column(name = "precio")
    private Double price;
    @Column(name = "fecha_disponible_desde")
    private LocalDate availableFrom;
    @Column(name = "fecha_disponible_hasta")
    private LocalDate availableTo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    
    @Column(name = "borrado")
    private Boolean isRemoved;
    
    public Room(String type, Double price, LocalDate availableFrom, LocalDate availableTo, Hotel hotel) {
        this.type = type;
        this.price = price;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.hotel = hotel;
        this.isRemoved = false;
    }
}
