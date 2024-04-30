package com.closure13k.aaronfmpt4.service;

import com.closure13k.aaronfmpt4.dto.request.ClientRequestDTO;
import com.closure13k.aaronfmpt4.model.Client;

import java.util.List;

public interface IClientService {
    List<Client> findOrCreateClients(List<ClientRequestDTO> clientDTOs);
    
    Client findOrCreateClient(ClientRequestDTO dto);
}
