package com.github.joao_felisberto.microservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joao_felisberto.microservice.IntegrationTest;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.repository.AddressRepository;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.joao_felisberto.microservice.TestUtil.createClientDTO;
import static com.github.joao_felisberto.microservice.TestUtil.createDistinctClientDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MiscControllerIT {

    private static final String SHORTCUT_URL = "/shortcut";
    private static final String FILTER_CLIENTS_URL = "/api/client/filter";

    @Autowired
    private MockMvc restClientMockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    private static final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    private static final List<Client> clients = List.of(
        clientMapper.clientDTOToClient(createDistinctClientDTO("aaA", CountryCode.PORTUGAL)),
        clientMapper.clientDTOToClient(createDistinctClientDTO("bbB", CountryCode.SPAIN)),
        clientMapper.clientDTOToClient(createDistinctClientDTO("ccC", CountryCode.UNITED_STATES)),
        clientMapper.clientDTOToClient(createDistinctClientDTO("ddD", CountryCode.PORTUGAL)),
        clientMapper.clientDTOToClient(createDistinctClientDTO("eeE", CountryCode.SPAIN)),
        clientMapper.clientDTOToClient(createDistinctClientDTO("ffF", CountryCode.UNITED_STATES))
    );

    @BeforeEach
    void setup() {
        addressRepository.saveAll(clients.stream().map(Client::getAddress).toList());
        clientRepository.saveAll(clients);
        clientRepository.flush();
    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
        clientRepository.flush();
    }

    //    @Test
//    @Transactional
    void testShortcutEndpoint() throws Exception {
        // fixme this requires full springboot test and authentication since it calls an endpoint over http
        // Create a ClientDTO
        final ClientDTO clientDTO = createClientDTO();

        // Perform POST request to shortcut endpoint
        MvcResult result = restClientMockMvc.perform(post(SHORTCUT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isOk())
            .andReturn();

        // Verify response
        final ClientDTO returnedClientDTO = om.readValue(result.getResponse().getContentAsString(), ClientDTO.class);
        Assertions.assertEquals(clientDTO, returnedClientDTO);
    }

    @Test
    @Transactional
    void testFilterClientsEndpoint() {
        final long cid = clients.get(0).getId();
        final long aid = clients.get(0).getAddress().getId();
        final Map<String, int[]> requestResponseMap = Map.of(
            "id.equals=" + cid, new int[]{0},
            "name.equals=aaA", new int[]{0},
            "nif.equals=bbB", new int[]{1},
            "countryCode.equals=PORTUGAL", new int[]{0, 3},
            "phoneNumber.equals=0", new int[]{0, 3},
            "addressId.equals=" + aid, new int[]{0}
        );

        requestResponseMap.forEach((k, v) -> {
            try {
                final ClientDTO[] res = om.readValue(
                    restClientMockMvc.perform(
                            get(String.format("%s?%s", FILTER_CLIENTS_URL, k))
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                    ClientDTO[].class
                );

                Assertions.assertNotNull(res);
                Assertions.assertEquals(
                    Arrays.stream(v).mapToObj(clients::get).map(clientMapper::clientToClientDTO).toList(),
                    Arrays.stream(res).toList(),
                    String.format("For criteria '%s' expected indexes %s", k, Arrays.toString(v))
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
