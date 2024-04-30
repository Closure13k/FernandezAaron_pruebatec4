package com.closure13k.aaronfmpt4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String nif;     // ? TODO: Considera una validación estricta de DNI. Si no, al menos el pattern.
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(nullable = false, length = 100)
    private String surname;
    
    @Column(nullable = false, length = 100)
    private String email;
    
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<FlightBooking> reservedFlights;
    
    @ManyToMany(mappedBy = "clients", fetch = FetchType.LAZY)
    private List<RoomBooking> reservedRooms;
    
    private Boolean isRemoved = false;
    
}
