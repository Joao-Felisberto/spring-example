package com.github.joao_felisberto.microservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joao_felisberto.microservice.IntegrationTest;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import com.github.joao_felisberto.microservice.service.api.dto.PhoneNumberDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
                "a"
            ),
            new PhoneNumberDTO(
                new BigDecimal(CountryCode.PORTUGAL.ordinal()),
                new BigDecimal(1)
            )
        );
//        return new ClientDTO(
//            "a",
//            "1",
//            null,
//            null
//        );
    }

    @Test
    @Transactional
    void createClient() throws Exception {
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
        // Assertions.assertEquals(databaseSizeBeforeCreate, clientRepository.count() - 1);
        final long currentCount = clientRepository.count();
        // assertThat(databaseSizeBeforeCreate + 1).isEqualTo(currentCount);
        Assertions.assertEquals(returnedClient, clientDTO);
        // assertAddressUpdatableFieldsEquals(returnedClient, getPersistedAddress(returnedAddress));

        // insertedAddress = returnedAddress;
    }
}
