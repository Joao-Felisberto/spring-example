package com.github.joao_felisberto.microservice.repository;

import com.github.joao_felisberto.microservice.domain.Client;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.github.joao_felisberto.microservice.TestUtil.createClientDTO;

public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

//    @Autowired
//    public ClientRepositoryTest(ClientRepository clientRepository, AddressRepository addressRepository) {
//        this.clientRepository = clientRepository;
//        this.addressRepository = addressRepository;
//    }

    @BeforeEach
    void cleanDB() {
        addressRepository.deleteAll();
        addressRepository.flush();
        clientRepository.deleteAll();
        clientRepository.flush();
    }

    @Test
    public void testFindExistingClientByNIF() throws Exception {
        final Client client = Client.fromDTO(createClientDTO());

        addressRepository.saveAndFlush(client.getAddress());
        clientRepository.saveAndFlush(client);

        final Optional<Client> retrieved = clientRepository.findBynif(client.getNif());
        Assertions.assertTrue(retrieved.isPresent());

        Assertions.assertEquals(client.getNif(), retrieved.orElseThrow().getNif());
    }

    @Test
    public void testFindNonExistingClientByNIF() throws Exception {
        final Client client = Client.fromDTO(createClientDTO());

        addressRepository.saveAndFlush(client.getAddress());
        clientRepository.saveAndFlush(client);

        final Optional<Client> retrieved = clientRepository.findBynif(client.getNif());
        Assertions.assertTrue(retrieved.isEmpty());
    }
}
