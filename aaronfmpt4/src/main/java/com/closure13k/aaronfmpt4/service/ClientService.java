package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.ClientRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.ClientResponseDTO;
import com.closure13k.aaronfmpt4.exception.DTOValidationException;
import com.closure13k.aaronfmpt4.model.Client;
import com.closure13k.aaronfmpt4.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing clients.
 */
@RequiredArgsConstructor
@Service
public class ClientService implements IClientService {
    private final ClientRepository clientRepository;
    
    /**
     * Find or create clients from a list of client DTOs.
     */
    @Override
    public List<Client> findOrCreateClients(List<ClientRequestDTO> clientDTOs) {
        if (clientDTOs.isEmpty()) {
            throw new DTOValidationException("Client list must not be empty");
        }
        return clientDTOs.stream()
                .map(this::findOrCreateClient)
                .toList();
    }
    
    /**
     * Find or create a client from a client DTO.
     */
    public Client findOrCreateClient(ClientRequestDTO dto) {
        if (dto == null) {
            throw new DTOValidationException("Client must not be null.");
        }
        if (dto.nif() == null) {
            throw new DTOValidationException("Client nif must not be null.");
        }
        return clientRepository.findByNif(dto.nif())
                .orElseGet(() -> clientRepository.save(fromDTO(dto)));
    }
    
    /**
     * Create a client from a client DTO.
     */
    public static Client fromDTO(ClientRequestDTO dto) {
        Client newClient = new Client();
        newClient.setNif(dto.nif());
        newClient.setName(dto.name());
        newClient.setSurname(dto.surname());
        newClient.setEmail(dto.email());
        return newClient;
    }
    
    /**
     * Convert a list of clients to a list of client DTOs.
     */
    public List<ClientResponseDTO> toClientDTOs(List<Client> clients) {
        return clients.stream()
                .map(this::toClientDTO)
                .toList();
    }
    
    /**
     * Convert a client to a client DTO.
     */
    public ClientResponseDTO toClientDTO(Client client) {
        return ClientResponseDTO.builder()
                .withName(client.getName())
                .withSurname(client.getSurname())
                .build();
    }
    
}
