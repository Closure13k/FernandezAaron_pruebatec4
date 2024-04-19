package com.closure13k.aaronfmpt4.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "HOTEL")
public class Hotel {
    
    //? TODO: Borrar a futuro y usar code.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "codigo")
    private String code; //! TODO: Implementar un algoritmo para generar el código.
    @Column(name = "nombre")
    private String name;
    @Column(name = "ciudad")
    private String city;
    @Column(name = "borrado")
    private Boolean isRemoved;
    
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<Room> rooms;
    
    public Hotel(String code, String name, String city) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.isRemoved = false;
    }
}
