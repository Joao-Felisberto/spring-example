package com.github.joao_felisberto.microservice.service;

import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.repository.AddressRepository;
import com.github.joao_felisberto.microservice.repository.ClientRepository;
import com.github.joao_felisberto.microservice.service.criteria.ClientCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.joao_felisberto.microservice.TestUtil.createDistinctClientDTO;


@SpringBootTest

//@DataJpaTest

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@TestPropertySource(properties = {
//    "spring.liquibase.enabled = false"
//})
class ClientQueryServiceUnitTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    //    @Autowired
    private ClientQueryService clientQueryService;


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
        clientQueryService = new ClientQueryService(clientRepository);

        addressRepository.saveAll(clients.stream().map(Client::getAddress).toList());
        clientRepository.saveAll(clients);
        clientRepository.flush();
    }

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
        clientRepository.flush();
    }

    @Test
    @Transactional
    void testFilterClientsEndpointObj() {
        final long cid = clients.get(0).getId();
        final long aid = clients.get(0).getAddress().getId();
        final Map<ClientCriteria, int[]> requestResponseMap = Map.of(
            isID(cid), new int[]{0},
            isName("aaA"), new int[]{0},
            isNIF("bbB"), new int[]{1},
            isCountry(CountryCode.PORTUGAL), new int[]{0, 3},
            isPhone(0L), new int[]{0, 3},
            isAddress(aid), new int[]{0},
            distinct(), new int[]{0, 1, 2, 3, 4, 5}
        );

        requestResponseMap.forEach((k, v) -> {
            final List<Client> res = clientQueryService.findByCriteria(k);

            Assertions.assertNotNull(res);
            Assertions.assertEquals(
                Arrays.stream(v).mapToObj(clients::get).map(clientMapper::clientToClientDTO).toList(),
                res.stream().map(clientMapper::clientToClientDTO).toList(),
                String.format("For criteria '%s' expected indexes %s", k, Arrays.toString(v))
            );
        });
    }


    private ClientCriteria isID(Long id) {
        ClientCriteria criteria = new ClientCriteria();
        criteria.setId(
            (LongFilter) new LongFilter()
                .setEquals(id)
        );
        return criteria;
    }

    private ClientCriteria isName(String name) {
        ClientCriteria criteria = new ClientCriteria();
        criteria.setName(
            (StringFilter) new StringFilter()
                .setEquals(name)
        );
        return criteria;
    }

    private ClientCriteria isNIF(String nif) {
        ClientCriteria criteria = new ClientCriteria();
        criteria.setNif(
            (StringFilter) new StringFilter()
                .setEquals(nif)
        );
        return criteria;
    }

    private ClientCriteria isCountry(CountryCode countryCode) {
        final ClientCriteria criteria = new ClientCriteria();
        criteria.setCountryCode(
            (ClientCriteria.CountryCodeFilter) new ClientCriteria.CountryCodeFilter()
                .setEquals(countryCode)
        );
        return criteria;
    }

    private ClientCriteria isPhone(Long phone) {
        ClientCriteria criteria = new ClientCriteria();
        criteria.setPhoneNumber(
            (LongFilter) new LongFilter()
                .setEquals(phone)
        );
        return criteria;
    }

    private ClientCriteria isAddress(Long id) {
        ClientCriteria criteria = new ClientCriteria();
        criteria.setAddressId(
            (LongFilter) new LongFilter()
                .setEquals(id)
        );
        return criteria;
    }

    private ClientCriteria distinct() {
        ClientCriteria criteria = new ClientCriteria();
        criteria.setDistinct(true);
        return criteria;
    }

}
