package com.closure13k.aaronfmpt4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "VUELO")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "codigo")
    private String code; // ? Implementar algoritmo para generar código de vuelo en base a origen, destino y fecha.
    @Column(name = "origen")
    private String origin;
    @Column(name = "destino")
    private String destination;
    @Column(name = "fecha_salida")
    private LocalDate departureDate;
    @Column(name = "tipo_asiento")
    private String seatType;
    @Column(name = "plazas_disponibles")
    private Integer availableSeats;
    @Column(name = "precio")
    private Double price;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "RESERVAS_VUELOS",
            joinColumns = @JoinColumn(name = "id_vuelo"),
            inverseJoinColumns = @JoinColumn(name = "id_cliente"))
    private List<Client> passengers;
    
    @Column(name = "borrado")
    private Boolean isRemoved = false;
    
    public Flight(String code, String origin, String destination, LocalDate departureDate,
                  String seatType, Integer availableSeats, Double price) {
        this.code = code;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.seatType = seatType;
        this.availableSeats = availableSeats;
        this.price = price;
    }
 

}
