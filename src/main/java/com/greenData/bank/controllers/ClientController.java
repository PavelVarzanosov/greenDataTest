package com.greenData.bank.controllers;

import com.greenData.bank.models.Client;
import com.greenData.bank.models.enums.LegalForms;
import com.greenData.bank.modelsDTO.FilterDTO;
import com.greenData.bank.services.clientService.IClientService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final static Logger LOGGER = Logger.getLogger(BankController.class.getName());

    private IClientService clientService;

    @Autowired
    public ClientController(@Qualifier("H2ClientImpl") IClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/newClient")
    public ResponseEntity<Client> newClient(@RequestParam String name, String shortName, String address, LegalForms legalForm) {
        Client client = clientService.saveClient(name, shortName, address, legalForm);
        return ResponseEntity.ok(client);
    }

    @GetMapping("{id}")
    public ResponseEntity<Client> getClientById(@PathVariable UUID id) {
        try {
            Client client = clientService.getClientById(id);
            return ResponseEntity.ok(client);
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateClient")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        try {
            Client updatedClient = clientService.updateClient(client);
            return ResponseEntity.ok(updatedClient);
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        try {
            clientService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            LOGGER.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/getClients")
    public ResponseEntity<List<Client>> getClients(@RequestBody FilterDTO filterDTO) {
        List<Client> clientList = clientService.getClients(filterDTO);
        return ResponseEntity.ok(clientList);
    }

}