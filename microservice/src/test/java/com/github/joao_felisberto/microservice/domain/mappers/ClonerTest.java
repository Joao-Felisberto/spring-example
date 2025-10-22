package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.github.joao_felisberto.microservice.TestUtil.createClientDTO;

class ClonerTest {

    private final Cloner cloner = Mappers.getMapper(Cloner.class);

    @Test
    void testCloneClient() {
        final Client original = new Client()
            .id(1L)
            .name("a")
            .nif("a")
            .countryCode(CountryCode.SPAIN)
            .phoneNumber(1L)
            .address(
                new Address()
                    .id(1L)
                    .city("a")
                    .country(CountryCode.PORTUGAL)
                    .postcode("a")
                    .stateProvince("a")
                    .streetOne("a")
                    .streetTwo("a")
                    .emailAddress("a@a.a")
            );
        final Client copy = cloner.clone(original);

        Assertions.assertEquals(original.getId(), copy.getId());
        Assertions.assertEquals(original.getName(), copy.getName());
        Assertions.assertEquals(original.getNif(), copy.getNif());
        Assertions.assertEquals(original.getCountryCode(), copy.getCountryCode());
        Assertions.assertEquals(original.getPhoneNumber(), copy.getPhoneNumber());

        Assertions.assertEquals(original.getAddress().getId(), copy.getAddress().getId());
        Assertions.assertEquals(original.getAddress().getCity(), copy.getAddress().getCity());
        Assertions.assertEquals(original.getAddress().getCountry(), copy.getAddress().getCountry());
        Assertions.assertEquals(original.getAddress().getPostcode(), copy.getAddress().getPostcode());
        Assertions.assertEquals(original.getAddress().getStateProvince(), copy.getAddress().getStateProvince());
        Assertions.assertEquals(original.getAddress().getStreetOne(), copy.getAddress().getStreetOne());
        Assertions.assertEquals(original.getAddress().getStreetTwo(), copy.getAddress().getStreetTwo());
        Assertions.assertEquals(original.getAddress().getEmailAddress(), copy.getAddress().getEmailAddress());
        Assertions.assertEquals(original.getAddress().getClients(), copy.getAddress().getClients());
    }

    @Test
    void testCloneAddress() {
        final Address original = new Address()
            .id(1L)
            .city("a")
            .country(CountryCode.PORTUGAL)
            .postcode("a")
            .stateProvince("a")
            .streetOne("a")
            .streetTwo("a")
            .emailAddress("a@a.a");
        final Address copy = cloner.clone(original);

        Assertions.assertEquals(original.getId(), copy.getId());
        Assertions.assertEquals(original.getCity(), copy.getCity());
        Assertions.assertEquals(original.getCountry(), copy.getCountry());
        Assertions.assertEquals(original.getPostcode(), copy.getPostcode());
        Assertions.assertEquals(original.getStateProvince(), copy.getStateProvince());
        Assertions.assertEquals(original.getStreetOne(), copy.getStreetOne());
        Assertions.assertEquals(original.getStreetTwo(), copy.getStreetTwo());
        Assertions.assertEquals(original.getEmailAddress(), copy.getEmailAddress());
        Assertions.assertEquals(original.getClients(), copy.getClients());
    }

    @Test
    void testCloneClientDTO() {
        final ClientDTO original = createClientDTO();
        final ClientDTO clone = cloner.clone(original);

        Assertions.assertEquals(original, clone);
    }

    @Test
    void testCloneAddressDTO() {
        final AddressDTO original = createClientDTO().getAddress();
        final AddressDTO clone = cloner.clone(original);

        Assertions.assertEquals(original, clone);
    }
}
