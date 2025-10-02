package com.github.joao_felisberto.microservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joao_felisberto.microservice.IntegrationTest;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
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

import java.math.BigDecimal;

import static com.github.joao_felisberto.microservice.web.rest.TestUtil.cloneAddressDTO;
import static com.github.joao_felisberto.microservice.web.rest.TestUtil.cloneClientDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
public class ClientControllerIT {
    private static final String ENTITY_API_URL = "/api/clients";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

//    @Autowired
//    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private static final ClientDTO NULL_CLIENT = new ClientDTO(null, null, null, null, null);

    public static ClientDTO createClientDTO() {
        return new ClientDTO(
            "a",
            "1",
            new AddressDTO(
                "a",
                new BigDecimal(CountryCode.PORTUGAL.ordinal()),
                "a",
                "a",
                "a",
                "a",
                "a@a.pt"
            ),
            new BigDecimal(CountryCode.PORTUGAL.ordinal()),
            new BigDecimal(1)
        );
    }

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
        // assertAddressUpdatableFieldsEquals(returnedClient, etPersistedAddress(returnedAddress)g);
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
            Assertions.assertEquals(returnedClient, NULL_CLIENT);
        }
    }
}
