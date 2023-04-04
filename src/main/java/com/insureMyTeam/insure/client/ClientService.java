package com.insureMyTeam.insure.client;

import com.insureMyTeam.insure.exceptions.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

  private final ClientRepository clientRepository;

  private final ClientMapper clientMapper;

   @Autowired public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = new Client();
        clientMapper.mapDTOtoEntity(clientDTO, client);
        clientRepository.save(client);
        return clientMapper.buildDTOFromEntity(client);
    }

    public List<ClientDTO> getALlClients() {
        List<Client> clientList = clientRepository.findAll();
        return clientList.stream().map(clientMapper::buildDTOFromEntity).collect(Collectors.toList());
    }

    public ClientDTO getClient(Long clientId) throws ClientNotFoundException {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new ClientNotFoundException();
        }
        return clientMapper.buildDTOFromEntity(client.get());
    }


    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) throws ClientNotFoundException {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new ClientNotFoundException();
        }
        clientMapper.mapDTOtoEntity(clientDTO, client.get());
        clientRepository.save(client.get());
        return clientMapper.buildDTOFromEntity(client.get());
    }

    public String deleteClient(Long clientId) throws ClientNotFoundException {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isEmpty()) {
            throw new ClientNotFoundException();
        }
        clientRepository.delete(client.get());
        return "Client of id : " + clientId + " deleted successfully";
    }
}
