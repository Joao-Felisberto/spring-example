package com.github.joao_felisberto.microservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joao_felisberto.microservice.IntegrationTest;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.repository.AddressRepository;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.joao_felisberto.microservice.TestUtil.*;
import static com.github.joao_felisberto.microservice.web.rest.TestUtil.cloneAddressDTO;
import static com.github.joao_felisberto.microservice.web.rest.TestUtil.cloneClientDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientControllerIT {
    private static final String ENTITY_API_URL = "/api/clients";
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
            cloneClientDTO(validClient).name(null),
            cloneClientDTO(validClient).nif(null),
            cloneClientDTO(validClient).address(null),
            cloneClientDTO(validClient).phoneNumber(null),
            cloneClientDTO(validClient).phoneCountryCode(null),

            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).city(null)),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).country(null)),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).postcode(null)),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).stateOrProvince(null)),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).streetOne(null)),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).streetTwo(null)),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).emailAddress(null)),

            // ---

            cloneClientDTO(validClient).name(""),
            cloneClientDTO(validClient).nif(""),

            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).city("")),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).postcode("")),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).stateOrProvince("")),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).streetOne("")),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).streetTwo("")),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).emailAddress("")),

            // todo proper email fuzzing

            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).emailAddress("a")),
            cloneClientDTO(validClient).address(cloneAddressDTO(validClient.getAddress()).emailAddress("a@")),
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
        final Client client = Client.fromDTO(clientDTO);

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
        final Client client = Client.fromDTO(clientDTO);

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
            Client.fromDTO(createDistinctClientDTO("a")),
            Client.fromDTO(createDistinctClientDTO("b")),
            Client.fromDTO(createDistinctClientDTO("c")),
            Client.fromDTO(createDistinctClientDTO("d")),
            Client.fromDTO(createDistinctClientDTO("e")),
            Client.fromDTO(createDistinctClientDTO("f"))
        );

        addressRepository.saveAll(cs.stream().map(Client::getAddress).toList());
        clientRepository.saveAll(cs);

        Assertions.assertEquals(cs.size(), addressRepository.count());
        Assertions.assertEquals(cs.size(), clientRepository.count());

        final Set<?> returnedClients = new HashSet<Object>(om.readValue(
            restClientMockMvc
                .perform(get(ENTITY_API_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            List.class
        ));

        Assertions.assertEquals(cs.size(), returnedClients.size());
        cs.forEach(c -> {
            final ClientDTO cdto = c.toDTO();
            LOG.debug("Is in response? {}", cdto);
            Assertions.assertTrue(returnedClients.contains(cdto));
        });
    }
}
