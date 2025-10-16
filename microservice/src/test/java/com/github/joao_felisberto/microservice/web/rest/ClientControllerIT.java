package com.github.joao_felisberto.microservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joao_felisberto.microservice.IntegrationTest;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.domain.mappers.Cloner;
import com.github.joao_felisberto.microservice.repository.AddressRepository;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.joao_felisberto.microservice.TestUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientControllerIT {
    private static final String ENTITY_API_URL = "/api/client";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private Cloner cloner;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private MockMvc restClientMockMvc;

    @BeforeEach
    void clearDB() {
        clientRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    @Transactional
    void createValidClient() throws Exception {
        final long clientDBSizeBeforeCreate = clientRepository.count();
        final long addressDBSizeBeforeCreate = addressRepository.count();
        final ClientDTO clientDTO = createClientDTO();

        final ClientDTO returnedClient = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        // Validate the Address in the database
        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count() - 1);
        Assertions.assertEquals(addressDBSizeBeforeCreate, addressRepository.count() - 1);
        Assertions.assertEquals(returnedClient, clientDTO);
    }

    @Test
    @Transactional
    void createValidClientWithDuplicateAddress() throws Exception {
        final long clientDBSizeBeforeCreate = clientRepository.count();
        final long addressDBSizeBeforeCreate = addressRepository.count();
        final ClientDTO firstClientDTO = createDistinctClientDTO("a");
        final AddressDTO addressDTO = firstClientDTO.getAddress();

        final ClientDTO firstRes = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(firstClientDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        // Validate the Address in the database
        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count() - 1);
        Assertions.assertEquals(addressDBSizeBeforeCreate, addressRepository.count() - 1);
        Assertions.assertEquals(firstRes, firstClientDTO);
        Assertions.assertEquals(firstRes.getAddress(), addressDTO);

        final ClientDTO secondClientDTO = createDistinctClientDTO("b");
        secondClientDTO.setAddress(addressDTO);
        Assertions.assertEquals(secondClientDTO.getAddress(), addressDTO);

        final ClientDTO secondRes = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(secondClientDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count() - 2);
        Assertions.assertEquals(addressDBSizeBeforeCreate, addressRepository.count() - 1);
        Assertions.assertEquals(secondRes, secondClientDTO);
        Assertions.assertEquals(secondRes.getAddress(), addressDTO);
    }

    @Test
    @Transactional
    void createValidClientWithDistinctAddress() throws Exception {
        final long clientDBSizeBeforeCreate = clientRepository.count();
        final long addressDBSizeBeforeCreate = addressRepository.count();
        final ClientDTO firstClientDTO = createDistinctClientDTO("a");

        final ClientDTO firstRes = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(firstClientDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        // Validate the Address in the database
        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count() - 1);
        Assertions.assertEquals(addressDBSizeBeforeCreate, addressRepository.count() - 1);
        Assertions.assertEquals(firstRes, firstClientDTO);

        final ClientDTO secondClientDTO = createDistinctClientDTO("b");
        Assertions.assertNotEquals(secondClientDTO.getAddress(), firstClientDTO.getAddress());

        final ClientDTO secondRes = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(secondClientDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count() - 2);
        Assertions.assertEquals(addressDBSizeBeforeCreate, addressRepository.count() - 2);
        Assertions.assertEquals(secondRes, secondClientDTO);
        Assertions.assertNotEquals(secondRes.getAddress(), firstRes.getAddress());
    }

    @Test
    @Transactional
    void createInvalidClient() throws Exception {
        final long databaseSizeBeforeCreate = clientRepository.count();
        final ClientDTO validClient = createClientDTO();

        final ClientDTO[] invalidClients = {
            cloner.clone(validClient).name(null),
            cloner.clone(validClient).nif(null),
            cloner.clone(validClient).address(null),
            cloner.clone(validClient).phoneNumber(null),
            cloner.clone(validClient).phoneCountryCode(null),

            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).city(null)),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).country(null)),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).postcode(null)),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).stateProvince(null)),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).streetOne(null)),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).streetTwo(null)),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).emailAddress(null)),

            // ---

            cloner.clone(validClient).name(""),
            cloner.clone(validClient).nif(""),

            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).city("")),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).postcode("")),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).stateProvince("")),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).streetOne("")),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).streetTwo("")),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).emailAddress("")),

            // todo proper email fuzzing

            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).emailAddress("a")),
            cloner.clone(validClient).address(cloner.clone(validClient.getAddress()).emailAddress("a@")),
        };

        for (final ClientDTO invalidClient : invalidClients) {
            final ClientDTO returnedClient = om.readValue(
                restClientMockMvc
                    .perform(post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(invalidClient))
                    )
                    .andExpect(status().isBadRequest())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(),
                ClientDTO.class
            );

            Assertions.assertEquals(databaseSizeBeforeCreate, clientRepository.count());
            Assertions.assertEquals(NULL_CLIENT, returnedClient);
        }
    }

    @Test
    @Transactional
    void deleteExistingClient() throws Exception {
        final long clientDBSizeBeforeCreate = clientRepository.count();
        final ClientDTO clientDTO = createClientDTO();
        final Client client = clientMapper.clientDTOToClient(clientDTO);

        addressRepository.saveAndFlush(client.getAddress());
        clientRepository.saveAndFlush(client);
        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count() - 1);

        final long id = client.getId();

        restClientMockMvc
            .perform(delete(String.format("%s/%d", ENTITY_API_URL, id)))
            .andExpect(status().isNoContent());

        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count());
    }

    @Test
    @Transactional
    void deleteNonExistingClient() throws Exception {
        final long clientDBSizeBeforeDelete = clientRepository.count();
        final long nonExistentID = -1;
        Assertions.assertTrue(clientRepository.findById(nonExistentID).isEmpty());

        restClientMockMvc
            .perform(delete(String.format("%s/%d", ENTITY_API_URL, nonExistentID)))
            .andExpect(status().isNoContent());

        Assertions.assertEquals(clientDBSizeBeforeDelete, clientRepository.count());
    }

    @Test
    @Transactional
    void deleteClientWithBadID() throws Exception {
        final long clientDBSizeBeforeDelete = clientRepository.count();
        final String nonExistentID = "This is not a valid ID!";

        restClientMockMvc
            .perform(delete(String.format("%s/%s", ENTITY_API_URL, nonExistentID)))
            .andExpect(status().isBadRequest());

        Assertions.assertEquals(clientDBSizeBeforeDelete, clientRepository.count());
    }

    @Test
    @Transactional
    void findExistingClientByNIF() throws Exception {
        final long clientDBSizeBeforeCreate = clientRepository.count();
        final ClientDTO clientDTO = createClientDTO();
        final Client client = clientMapper.clientDTOToClient(clientDTO);

        addressRepository.saveAndFlush(client.getAddress());
        clientRepository.saveAndFlush(client);
        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count() - 1);

        final String nif = client.getNif();

        final ClientDTO returnedClient = om.readValue(
            restClientMockMvc
                .perform(get(String.format("%s/%s", ENTITY_API_URL, nif))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        Assertions.assertEquals(returnedClient, clientDTO);

        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count() - 1);
    }

    @Test
    @Transactional
    void findNonExistingClientByNIF() throws Exception {
        final long clientDBSizeBeforeCreate = clientRepository.count();
        final String nonExistentNIF = "I do not exist!";
        Assertions.assertTrue(clientRepository.findBynif(nonExistentNIF).isEmpty());

        restClientMockMvc
            .perform(get(String.format("%s/%s", ENTITY_API_URL, nonExistentNIF)))
            .andExpect(status().isNotFound());

        Assertions.assertEquals(clientDBSizeBeforeCreate, clientRepository.count());
    }

    @Test
    @Transactional
    void listAllClientsEmpty() throws Exception {
        Assertions.assertEquals(0, clientRepository.count());

        final List<?> returnedClients = om.readValue(
            restClientMockMvc
                .perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            List.class
        );

        Assertions.assertEquals(0, returnedClients.size());
    }

    @Test
    @Transactional
    void listAllClientsNonEmpty() throws Exception {
        Assertions.assertEquals(0, clientRepository.count());

        final List<Client> cs = Arrays.asList(
            clientMapper.clientDTOToClient(createDistinctClientDTO("a")),
            clientMapper.clientDTOToClient(createDistinctClientDTO("b")),
            clientMapper.clientDTOToClient(createDistinctClientDTO("c")),
            clientMapper.clientDTOToClient(createDistinctClientDTO("d")),
            clientMapper.clientDTOToClient(createDistinctClientDTO("e")),
            clientMapper.clientDTOToClient(createDistinctClientDTO("f"))
        );

        addressRepository.saveAll(cs.stream().map(Client::getAddress).toList());
        clientRepository.saveAll(cs);

        Assertions.assertEquals(cs.size(), addressRepository.count());
        Assertions.assertEquals(cs.size(), clientRepository.count());

        final ClientDTO[] res = om.readValue(
            restClientMockMvc
                .perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO[].class
        );
        final Set<ClientDTO> returnedClients = Arrays.stream(res)
            .collect(Collectors.toSet());

        Assertions.assertEquals(cs.size(), returnedClients.size());
        cs.forEach(c -> Assertions.assertTrue(returnedClients.contains(clientMapper.clientToClientDTO(c))));
    }
}
