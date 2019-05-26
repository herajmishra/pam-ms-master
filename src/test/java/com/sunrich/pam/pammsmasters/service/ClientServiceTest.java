package com.sunrich.pam.pammsmasters.service;

import com.sunrich.pam.pammsmasters.domain.Client;
import com.sunrich.pam.pammsmasters.exception.ClientNotFoundException;
import com.sunrich.pam.pammsmasters.repository.ClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClientServiceTest {

    @InjectMocks
    ClientService clientService;

    @Mock
    ClientRepository clientRepository;

    @Test
    public void findAll_whenRecordsExists_shouldReturnRecords() {
        List<Client> clientList = Arrays.asList(
                Client.builder().id(UUID.randomUUID().toString()).name("Client 1").recordStatus(true).build(),
                Client.builder().id(UUID.randomUUID().toString()).name("Client 2").recordStatus(true).build(),
                Client.builder().id(UUID.randomUUID().toString()).name("Client 3").recordStatus(true).build()
        );
        when(clientRepository.findAllByRecordStatusTrue()).thenReturn(clientList);
        assertThat(clientList).isEqualTo(clientService.findAll());
        verify(clientRepository).findAllByRecordStatusTrue();
    }

    @Test
    public void findById_whenRecordWithGivenIdExists_shouldReturnCorrespondingObject() {
        String id = UUID.randomUUID().toString();
        Client client = Client.builder().id(id).name("Client 1").recordStatus(true).build();
        when(clientRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(client));
        assertThat(client).isEqualTo(clientService.findById(id));
        verify(clientRepository).findByIdAndRecordStatusTrue(id);
    }

    @Test(expected = ClientNotFoundException.class)
    public void findById_whenRecordWithGivenIdDoesNotExist_shouldThrowNotFoundException() {
        String id = UUID.randomUUID().toString();
        clientService.findById(id);
        verify(clientRepository).findByIdAndRecordStatusTrue(id);
    }

    @Test
    public void findByName_whenRecordWithGivenNameExists_shouldReturnCorrespondingObject() {
        Client client = Client.builder().id(UUID.randomUUID().toString()).name("Client 2").recordStatus(true).build();
        when(clientRepository.findByNameAndRecordStatusTrue("Client 2")).thenReturn(Optional.ofNullable(client));
        assertThat(client).isEqualTo(clientService.findByName("Client 2"));
        verify(clientRepository).findByNameAndRecordStatusTrue("Client 2");
    }

    @Test
    public void save_whenValidObject_shouldSaveAndReturnSavedObject() {
        Client.ClientBuilder builder = Client.builder().id(null).name("Client 1").recordStatus(false);

        Client newClient = builder.build();
        Client clientToSave = builder.recordStatus(true).build();
        Client savedClient = builder.id(UUID.randomUUID().toString()).recordStatus(true).build();

        when(clientRepository.save(any())).thenReturn(savedClient);
        assertThat(savedClient).isEqualTo(clientService.saveOrUpdate(newClient));
        verify(clientRepository).save(clientToSave);
    }

    @Test
    public void update_whenValidObject_shouldSaveAndReturnSavedObject() {
        String id = UUID.randomUUID().toString();

        Client.ClientBuilder builder = Client.builder().id(id).name("Client 1").recordStatus(false);

        Client user = builder.build();
        Client userToUpdate = builder.recordStatus(true).build();

        when(clientRepository.save(userToUpdate)).thenReturn(userToUpdate);
        assertThat(userToUpdate).isEqualTo(clientService.saveOrUpdate(user));
        verify(clientRepository).save(userToUpdate);
    }

    @Test
    public void delete_whenRecordWithGivenIdExists_shouldUpdateRecordStatusToFalseAndReturnTheId() {
        String id = UUID.randomUUID().toString();

        Client.ClientBuilder builder = Client.builder().id(id).name("Client 1");

        Client client = builder.recordStatus(true).build();
        when(clientRepository.findByIdAndRecordStatusTrue(id)).thenReturn(Optional.ofNullable(client));

        Client clientToDelete = builder.recordStatus(false).build();
        when(clientRepository.save(clientToDelete)).then(returnsFirstArg());

        assertThat(id).isEqualTo(clientService.delete(id));

        verify(clientRepository).findByIdAndRecordStatusTrue(id);
        verify(clientRepository).save(clientToDelete);
    }
}
