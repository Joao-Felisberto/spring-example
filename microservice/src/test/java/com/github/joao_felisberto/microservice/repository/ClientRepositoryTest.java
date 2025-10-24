package com.github.joao_felisberto.microservice.repository;

import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.github.joao_felisberto.microservice.TestUtil.createDistinctClientDTO;

@SpringBootTest
class ClientRepositoryTest {

    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
        clientRepository.flush();
        addressRepository.deleteAll();
        addressRepository.flush();
    }

    @Test
    void testFindExistingClientByNIF() {
        final Client client = clientMapper.clientDTOToClient(createDistinctClientDTO("a"));

        addressRepository.save(client.getAddress());
        clientRepository.save(client);

        final Optional<Client> retrieved = clientRepository.findBynif(client.getNif());
        Assertions.assertTrue(retrieved.isPresent());

        Assertions.assertEquals(client.getNif(), retrieved.orElseThrow().getNif());
    }

    @Test
    void testFindNonExistingClientByNIF() {
        final Client client = clientMapper.clientDTOToClient(createDistinctClientDTO("b"));

        addressRepository.save(client.getAddress());
        clientRepository.save(client);

        final Optional<Client> retrieved = clientRepository.findBynif("INVALID NIF");
        Assertions.assertTrue(retrieved.isEmpty());
    }
}
