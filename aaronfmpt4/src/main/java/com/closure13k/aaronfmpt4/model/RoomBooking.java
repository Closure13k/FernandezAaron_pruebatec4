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
@Entity
public class RoomBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "room_booking_client",
            joinColumns = @JoinColumn(name = "room_booking_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> clients;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;
}