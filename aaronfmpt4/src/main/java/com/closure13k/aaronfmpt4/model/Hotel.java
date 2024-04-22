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
    private Long id;
    
    @Column(name = "codigo", unique = true, nullable = false, length = 10)
    private String code; //! TODO: Implementar un algoritmo para generar el código.
    
    @Column(name = "nombre", nullable = false)
    private String name;
    
    @Column(name = "ciudad", nullable = false)
    private String city;
    
    @Column(name = "borrado", nullable = false)
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
