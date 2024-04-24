package com.closure13k.aaronfmpt4.repository;

import com.closure13k.aaronfmpt4.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

}
