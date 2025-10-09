package com.github.joao_felisberto.microservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joao_felisberto.microservice.IntegrationTest;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.repository.AddressRepository;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import com.github.joao_felisberto.microservice.util.Cloner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(ClientControllerIT.class);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

//    @Autowired
//    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    @BeforeEach
    void clearDB() {
        clientRepository.deleteAll();
    }

    @Test
    @Transactional
    void createValidClient() throws Exception {
        final long databaseSizeBeforeCreate = clientRepository.count();
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
        Assertions.assertEquals(databaseSizeBeforeCreate, clientRepository.count() - 1);
        Assertions.assertEquals(returnedClient, clientDTO);
    }

    @Test
    @Transactional
    void createInvalidClient() throws Exception {
        final long databaseSizeBeforeCreate = clientRepository.count();
        final ClientDTO validClient = createClientDTO();

        final ClientDTO[] invalidClients = {
            Cloner.INSTANCE.clone(validClient).name(null),
            Cloner.INSTANCE.clone(validClient).nif(null),
            Cloner.INSTANCE.clone(validClient).address(null),
            Cloner.INSTANCE.clone(validClient).phoneNumber(null),
            Cloner.INSTANCE.clone(validClient).phoneCountryCode(null),

            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).city(null)),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).country(null)),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).postcode(null)),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).stateOrProvince(null)),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).streetOne(null)),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).streetTwo(null)),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).emailAddress(null)),

            // ---

            Cloner.INSTANCE.clone(validClient).name(""),
            Cloner.INSTANCE.clone(validClient).nif(""),

            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).city("")),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).postcode("")),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).stateOrProvince("")),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).streetOne("")),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).streetTwo("")),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).emailAddress("")),

            // todo proper email fuzzing

            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).emailAddress("a")),
            Cloner.INSTANCE.clone(validClient).address(Cloner.INSTANCE.clone(validClient.getAddress()).emailAddress("a@")),
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
        final Client client = ClientMapper.INSTANCE.clientDTOToClient(clientDTO);

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
        final Client client = ClientMapper.INSTANCE.clientDTOToClient(clientDTO);

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
            ClientMapper.INSTANCE.clientDTOToClient(createDistinctClientDTO("a")),
            ClientMapper.INSTANCE.clientDTOToClient(createDistinctClientDTO("b")),
            ClientMapper.INSTANCE.clientDTOToClient(createDistinctClientDTO("c")),
            ClientMapper.INSTANCE.clientDTOToClient(createDistinctClientDTO("d")),
            ClientMapper.INSTANCE.clientDTOToClient(createDistinctClientDTO("e")),
            ClientMapper.INSTANCE.clientDTOToClient(createDistinctClientDTO("f"))
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
        cs.forEach(c -> Assertions.assertTrue(returnedClients.contains(ClientMapper.INSTANCE.clientToClientDTO(c))));
    }
}
