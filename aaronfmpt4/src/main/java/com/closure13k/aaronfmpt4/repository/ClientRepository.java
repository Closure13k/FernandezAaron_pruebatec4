package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByNif(String nif);
}
