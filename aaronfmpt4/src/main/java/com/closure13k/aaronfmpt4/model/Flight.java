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
@Entity(name = "VUELO")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "codigo", unique = true, nullable = false, length = 20)
    private String code; // ? TODO: Implementar algoritmo para generar código de vuelo en base a origen, destino y fecha.
    
    @Column(name = "origen", nullable = false, length = 100)
    private String origin;
    
    @Column(name = "destino", nullable = false, length = 100)
    private String destination;
    
    @Column(name = "fecha_salida", nullable = false)
    private LocalDate departureDate;
    
    @Column(name = "tipo_asiento", nullable = false)
    private String seatType;
    
    @Column(name = "plazas_disponibles", nullable = false)
    private Integer availableSeats;
    
    @Column(name = "precio", nullable = false, precision = 7, scale = 2)
    private BigDecimal price;
    
    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY)
    private List<FlightBooking> bookingList;
    
    @Column(name = "borrado")
    private Boolean isRemoved = false;
    
    public Flight(String code, String origin, String destination, LocalDate departureDate,
                  String seatType, Integer availableSeats, BigDecimal price) {
        this.code = code;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.seatType = seatType;
        this.availableSeats = availableSeats;
        this.price = price;
    }
    
    
}
