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
@Entity(name = "HABITACION")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo", nullable = false, length = 10)
    private String code;
    
    @Column(name = "tipo", nullable = false, length = 50)
    private String type;
    
    @Column(name = "precio", nullable = false, precision = 7, scale = 2)
    private BigDecimal price;
    
    @Column(name = "fecha_disponible_desde", nullable = false)
    private LocalDate availableFrom;
    
    @Column(name = "fecha_disponible_hasta", nullable = false)
    private LocalDate availableTo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
    
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomBooking> bookingList;
    
    @Column(name = "borrado")
    private Boolean isRemoved;
    
    public Room(String type, BigDecimal price, LocalDate availableFrom, LocalDate availableTo, Hotel hotel) {
        this.type = type;
        this.price = price;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.hotel = hotel;
        this.isRemoved = false;
    }
}
