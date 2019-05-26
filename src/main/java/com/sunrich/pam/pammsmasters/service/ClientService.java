package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Client;
import com.sunrich.pam.pammsmasters.exception.ClientNotFoundException;
import com.sunrich.pam.pammsmasters.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    /**
     * Used to save or update the client
     *
     * @param client - domain object
     * @return - the saved/updated client
     */
    public Client saveOrUpdate(Client client) {
        client.setRecordStatus(true);
        return clientRepository.save(client);
    }

    /**
     * Used to get the list of clients
     *
     * @return - the list of clients
     */
    public List<Client> findAll() {
        return clientRepository.findAllByRecordStatusTrue();
    }

    /**
     * Used to get the client by id
     *
     * @param id - client identifier
     * @return - client object
     */
    public Client findById(String id) {
        Optional<Client> optClient = clientRepository.findByIdAndRecordStatusTrue(id);
        if (!optClient.isPresent()) {
            throw new ClientNotFoundException("Client Not Found!");
        }
        return optClient.get();
    }

    /**
     * Used to get the client by name
     *
     * @param name - client name
     * @return - client object
     */
    public Client findByName(String name) {
        Optional<Client> optClient = clientRepository.findByNameAndRecordStatusTrue(name);
        if (!optClient.isPresent()) {
            throw new ClientNotFoundException("Client Not Found!");
        }
        return optClient.get();
    }

    /**
     * Used to logically delete the client by updating recordStatus flag to false
     *
     * @param id - client identifier
     * @return - removed client identifier
     */
    public String delete(String id) {
        Client client = findById(id);
        client.setRecordStatus(false);
        clientRepository.save(client);
        return client.getId();
    }
}
