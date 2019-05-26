package com.sunrich.pam.pammsmasters.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunrich.pam.pammsmasters.domain.Client;
import com.sunrich.pam.pammsmasters.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody String strClient) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = objectMapper.readValue(strClient, Client.class);
        if (client.getId() == null) {
            client.setId(UUID.randomUUID().toString());
        }
        return ResponseEntity.ok().body(clientService.saveOrUpdate(client));
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll() {
        List<Client> clientList = clientService.findAll();
        return ResponseEntity.ok().body(clientList);
    }

    @GetMapping("/findById")
    public ResponseEntity findById(@RequestParam String id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok().body(client);
    }

    @GetMapping("/findByName")
    public ResponseEntity findByName(@RequestParam String name) {
        Client client = clientService.findByName(name);
        return ResponseEntity.ok().body(client);
    }

    @GetMapping("/delete")
    public ResponseEntity delete(@RequestParam String id) {
        clientService.delete(id);
        return ResponseEntity.ok().body("Client removed");
    }
}
