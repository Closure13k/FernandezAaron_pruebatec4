package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.ClientRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.ClientResponseDTO;
import com.closure13k.aaronfmpt4.model.Client;
import com.closure13k.aaronfmpt4.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientService implements IClientService {
    private final ClientRepository clientRepository;
    
    @Override
    public List<Client> findOrCreateClients(List<ClientRequestDTO> clientDTOs) {
        return clientDTOs.stream()
                .map(this::findOrCreateClient)
                .toList();
    }
    
    public Client findOrCreateClient(ClientRequestDTO dto) {
        return clientRepository.findByNif(dto.nif())
                .orElseGet(() -> clientRepository.save(fromDTO(dto)));
    }
    
    public static Client fromDTO(ClientRequestDTO dto) {
        Client newClient = new Client();
        newClient.setNif(dto.nif());
        newClient.setName(dto.name());
        newClient.setSurname(dto.surname());
        newClient.setEmail(dto.email());
        return newClient;
    }
    
    
    public List<ClientResponseDTO> toClientDTOs(List<Client> clients) {
        return clients.stream()
                .map(this::toClientDTO)
                .toList();
    }
    
    public ClientResponseDTO toClientDTO(Client client) {
        return ClientResponseDTO.builder()
                .withName(client.getName())
                .withSurname(client.getSurname())
                .build();
    }
    
}
