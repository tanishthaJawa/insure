package com.insureMyTeam.insure.serviceTests;

import com.insureMyTeam.insure.client.*;
import com.insureMyTeam.insure.exceptions.ClientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ClientServiceTest {
    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private ClientMapper clientMapper;

    private ClientService clientService;

    private Client client;

    private ClientDTO clientDTO;

    @BeforeEach
    public void setUp() {
        clientService = new ClientService(clientRepository, clientMapper);
        clientDTO = ClientDTO.builder()
                .name("John Doe")
                .dateOfBirth(new Date(2001111))
                .address("New York USA")
                .contactNumber("9877654112")
                .build();

        client = new Client(1L, "Daisy Doe", new Date(2001111), "New York USA", "9877654112", new ArrayList<>());


    }


    @Test
    public void getAllClients()  {
        List<Client> clients = List.of(new Client(1L, "Bob", new Date(), "6b, karol Bagh, delhi", "9876543197", new ArrayList<>()),
                new Client(2L, "Anna", new Date(), "6b, karol Bagh, delhi", "9876543197", new ArrayList<>()));
        List<ClientDTO> expectedClients = clients.stream().map(client1 -> clientMapper.buildDTOFromEntity(client1)).collect(Collectors.toList());
        when(clientRepository.findAll()).thenReturn(clients);

        List<ClientDTO> clientsList = clientService.getALlClients();

        assertEquals(expectedClients, clientsList);
        verify(clientRepository).findAll();

    }

    @Test
    public void shouldGetClientIfFound() throws ClientNotFoundException {
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));

        ClientDTO expectedClient = clientService.getClient(1L);

        assertEquals(expectedClient, clientMapper.buildDTOFromEntity(client));
        verify(clientRepository).findById(1L);

    }

    @Test
    public void shouldNotGetClientAndThrowExceptionWhenClientDoesNotExist() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getClient(client.getId()));

    }

    @Test
    public void shouldDeleteClientIfFound() throws ClientNotFoundException {
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));

        clientService.deleteClient(1L);

        verify(clientRepository).delete(client);
    }

    @Test
    public void shouldNotDeleteAndThrowExceptionWhenClientDoesNotExist() throws ClientNotFoundException {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.deleteClient(client.getId()));
        verify(clientRepository, never()).delete(client);
    }

    @Test
    public void shouldUpdateClientIfFound() throws ClientNotFoundException {
        when(clientRepository.findById(client.getId())).thenReturn(Optional.of(client));

        clientService.updateClient(client.getId(), clientDTO);

        verify(clientRepository).save(client);
        verify(clientRepository).findById(client.getId());
    }

    @Test
    public void shouldNotUpdateAndThrowExceptionWhenClientDoesNotExist() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(client.getId(), clientDTO));
        verify(clientRepository, never()).save(client);
    }

    @Test
    public void shouldCreateClientSuccessfully() {
        ClientDTO clientDTO1 = ClientDTO.builder().
                name(client.getName()).
                policyList(client.getPolicyList()).
                contactNumber(client.getContactNumber()).
                dateOfBirth(client.getDateOfBirth())
                .address(client.getAddress())
                .build();

        clientService.createClient(clientDTO1);

        verify(clientRepository).save(any(Client.class));
    }
}
