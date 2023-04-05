package com.insureMyTeam.insure.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insureMyTeam.insure.claims.ClaimDTO;
import com.insureMyTeam.insure.client.ClientController;
import com.insureMyTeam.insure.client.ClientDTO;
import com.insureMyTeam.insure.client.ClientService;
import com.insureMyTeam.insure.exceptions.ClientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@RunWith(SpringRunner.class)
public class ClientControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ClientService clientService;

    private ClientDTO client;

    @BeforeEach
    public void init() {
        client = ClientDTO.builder()
                .name("John Doe")
                .dateOfBirth(new Date(2001111))
                .address("New York USA")
                .contactNumber("9877654112")
                .build();
    }
    @Test
    @DisplayName("Should get all clients successfully")
    public void shouldGetAllClientsSuccessfully() throws Exception {
        List<ClientDTO> clients = new ArrayList<>(
                List.of(new ClientDTO(1L, "Bob", new Date(), "6b, karol Bagh, delhi", "9876543197", new ArrayList<>()),
                        new ClientDTO(2L, "Anna", new Date(), "6b, karol Bagh, delhi", "9876543197", new ArrayList<>())));

        when(clientService.getALlClients()).thenReturn(clients);

        mockMvc.perform(get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Bob"));
    }

    @Test
    @DisplayName("Should create client successfully")
    public void shouldCreateUserSuccessfully() throws Exception {
        when(clientService.createClient(client)).thenReturn(client);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(client)))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Should get a client successfully")
    public void shouldGetClientSuccessfully() throws Exception {
        when(clientService.getClient(1L)).thenReturn(client);

        mockMvc.perform(get("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @DisplayName("Should not get a client successfully")
    public void shouldNotGetClientSuccessfully() throws Exception {
        doThrow(new ClientNotFoundException()).when(clientService).getClient(1L);

        mockMvc.perform(get("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("Should update client successfully")
    public void shouldUpdateClientSuccessfully() throws Exception {
        ClientDTO updatedClient = ClientDTO.builder()
                .name("Daisy Doe")
                .dateOfBirth(new Date(2001111))
                .address("New York USA")
                .contactNumber("9877654112")
                .build();

        when(clientService.updateClient(1L, updatedClient)).thenReturn(updatedClient);

        mockMvc.perform(put("/api/clients/1").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(this.mapper.writeValueAsString(updatedClient)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Should delete client successfully")
    public void shouldDeleteClientSuccessfully() throws Exception {
        when(clientService.deleteClient(1L)).thenReturn("Client of id : " + 1 + " deleted successfully");

        mockMvc.perform(delete("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print()).
                andExpect(result -> assertEquals("Client of id : " + 1 + " deleted successfully", result.getResponse().getContentAsString()));
    }

    @Test
    @DisplayName("Should throw client not found exception and not delete client if client does not exist ")
    public void shouldNotDeleteClientWhenClientDoesNotExist() throws Exception {
        doThrow(new ClientNotFoundException()).when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andDo(print());
    }
}
