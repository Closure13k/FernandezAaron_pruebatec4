package com.closure13k.aaronfmpt4.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CLIENTE")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nif")
    private String nif;
    @Column(name = "nombre")
    private String name;
    @Column(name = "apellidos")
    private String surname;
    @Column(name = "email")
    private String email;
    
    @ManyToMany(mappedBy = "passengers", fetch = FetchType.LAZY)
    @Column(name = "vuelos_reservados")
    private List<Flight> reservedFlights;
    
    public Client(String nif, String name, String surname, String email) {
        this.nif = nif;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.reservedFlights = new ArrayList<>();
    }
}
