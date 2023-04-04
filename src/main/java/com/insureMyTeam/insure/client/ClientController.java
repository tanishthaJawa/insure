package com.insureMyTeam.insure.client;

import com.insureMyTeam.insure.exceptions.ClientNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Slf4j
public class ClientController {
    public ClientController() {
        log.info("Client class created");
    }

    @Autowired
    ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getClients() {
        List<ClientDTO> clientDTOList = clientService.getALlClients();
        return new ResponseEntity<>(clientDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable("id") Long clientId) throws ClientNotFoundException {
        ClientDTO client = clientService.getClient(clientId);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        ClientDTO client = clientService.createClient(clientDTO);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable("id") Long clientId, @RequestBody ClientDTO ClientDTO) throws ClientNotFoundException {
        ClientDTO client = clientService.updateClient(clientId, ClientDTO);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClaim(@PathVariable("id") Long clientId) throws ClientNotFoundException {
        String clientDeletionMessage = clientService.deleteClient(clientId);
        return new ResponseEntity<>(clientDeletionMessage, HttpStatus.OK);
    }
}
