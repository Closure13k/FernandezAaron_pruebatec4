package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.ClientRequestDTO;
import com.closure13k.aaronfmpt4.dto.response.ClientResponseDTO;
import com.closure13k.aaronfmpt4.exception.DTOValidationException;
import com.closure13k.aaronfmpt4.model.Client;
import com.closure13k.aaronfmpt4.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    
    @Mock
    private ClientRepository clientRepository;
    
    @InjectMocks
    private ClientService clientService;
    
    @Test
    void GivenClient_WhenCreateClient_ThenReturnClientWithId() {
        // Given
        ClientRequestDTO clientDTO = new ClientRequestDTO("John Doe", "123456789", "Doe", "John@Doe.com");
        when(clientRepository.findByNif(clientDTO.nif())).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client client = invocation.getArgument(0);
            client.setId(1L);
            return client;
        });
        
        // When
        Client createdClient = clientService.findOrCreateClient(clientDTO);
        
        // Then
        assertNotNull(createdClient.getId(), "On create, client ID must not be null");
        verify(clientRepository).save(any(Client.class));
    }
    
    @Test
    void GivenClient_WhenCreateClient_ThenReturnExistingClient() {
        // Given
        ClientRequestDTO clientDTO = new ClientRequestDTO("John Doe", "123456789", "Doe", "John@Doe.com");
        Client existingClientInDb = new Client();
        existingClientInDb.setId(1L); // Assign an ID to the existing client
        when(clientRepository.findByNif(clientDTO.nif())).thenReturn(Optional.of(existingClientInDb));
        
        // When
        Client existingClient = clientService.findOrCreateClient(clientDTO);
        
        // Then
        assertNotNull(existingClient.getId(), "On find, client ID must not be null");
        assertTrue(existingClient.getId() > 0, "On find, client ID must be greater than 0");
        
        verify(clientRepository).findByNif(clientDTO.nif());
    }
    
    @Test
    void GivenNullClient_WhenCreateClient_ThenThrowException() {
        // Given
        ClientRequestDTO clientDTO = null;
        // When & Then
        Exception exception = assertThrows(DTOValidationException.class, () -> clientService.findOrCreateClient(clientDTO));
        assertEquals("Client must not be null.", exception.getMessage());
    }
    
    @Test
    void GivenNonNullClientWithNullNif_WhenCreateClient_ThenThrowException() {
        // Given
        ClientRequestDTO clientDTO = new ClientRequestDTO(null, null, null, null);
        // When & Then
        Exception exception = assertThrows(DTOValidationException.class, () -> clientService.findOrCreateClient(clientDTO));
        assertEquals("Client nif must not be null.", exception.getMessage());
    }
    
    @Test
    void GivenClient_FromDTO_ThenReturnClient() {
        // Given
        ClientRequestDTO clientDTO = new ClientRequestDTO("John Doe", "123456789", "Doe", "");
        
        // When
        Client client = ClientService.fromDTO(clientDTO);
        
        // Then
        assertEquals(clientDTO.nif(), client.getNif());
        assertEquals(clientDTO.name(), client.getName());
        assertEquals(clientDTO.surname(), client.getSurname());
        assertEquals(clientDTO.email(), client.getEmail());
    }
    
    @Test
    void GivenClient_ToClientDTO_ThenReturnClientResponseDTO() {
        // Given
        Client client = new Client();
        client.setName("John");
        client.setSurname("Doe");
        
        // When
        ClientResponseDTO clientDTO = clientService.toClientDTO(client);
        
        // Then
        assertEquals(client.getName(), clientDTO.name());
        assertEquals(client.getSurname(), clientDTO.surname());
    }
}
