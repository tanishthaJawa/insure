package com.insureMyTeam.insure.client;

import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public void mapDTOtoEntity(ClientDTO clientDTO, Client client) {
        client.setAddress(clientDTO.getAddress());
        client.setName(clientDTO.getName());
        client.setContactNumber(clientDTO.getContactNumber());
        client.setDateOfBirth(clientDTO.getDateOfBirth());
    }

    public ClientDTO buildDTOFromEntity(Client savedClient) {
        return ClientDTO.builder().name(savedClient.getName())
                .address(savedClient.getAddress())
                .contactNumber(savedClient.getContactNumber())
                .dateOfBirth(savedClient.getDateOfBirth())
                .name(savedClient.getName())
                .id(savedClient.getId())
                .policyList(savedClient.getPolicyList())
                .build();
    }
}
