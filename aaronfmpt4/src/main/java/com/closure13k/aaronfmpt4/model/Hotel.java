package com.closure13k.aaronfmpt4.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 8)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String city;
    
    private Boolean isRemoved = false;
    
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<Room> rooms;
    
    
    /**
     * Generate a code for the hotel if one is not provided.
     * @param characters 4 characters extracted from name and city.
     * @param codeCount The count of hotel codes starting with the same 4 characters.
     */
    public void generateCode(String characters, Integer codeCount) {
        this.code = characters + String.format("%04d", codeCount);
    }
}
