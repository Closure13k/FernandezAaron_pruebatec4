package com.closure13k.aaronfmpt4.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<RoomBooking> reservedRooms;
    
    private Boolean isRemoved = false;
    
    public Client(String nif, String name, String surname, String email) {
        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.reservedFlights = new ArrayList<>();
    }
}
