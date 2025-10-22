package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static com.github.joao_felisberto.microservice.TestUtil.createClient;
import static com.github.joao_felisberto.microservice.TestUtil.createClientDTO;

class ClientMapperTest {

    private ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @Test
    void testClientToDTOMap() {
        final Client original = createClient();
        final ClientDTO converted = clientMapper.clientToClientDTO(original);

        Assertions.assertEquals(original.getName(), converted.getName());
        Assertions.assertEquals(original.getNif(), converted.getNif());
        Assertions.assertEquals(new BigDecimal(original.getPhoneNumber()), converted.getPhoneNumber());
        Assertions.assertEquals(original.getCountryCode().ordinal(), converted.getPhoneCountryCode().intValue());

        final Address originalAddr = original.getAddress();
        final AddressDTO convertedAddr = converted.getAddress();

        Assertions.assertEquals(originalAddr.getCountry().ordinal(), convertedAddr.getCountry().intValue());
        Assertions.assertEquals(originalAddr.getCity(), convertedAddr.getCity());
        Assertions.assertEquals(originalAddr.getStreetOne(), convertedAddr.getStreetOne());
        Assertions.assertEquals(originalAddr.getStreetTwo(), convertedAddr.getStreetTwo());
        Assertions.assertEquals(originalAddr.getPostcode(), convertedAddr.getPostcode());
        Assertions.assertEquals(originalAddr.getStateProvince(), convertedAddr.getStateProvince());
        Assertions.assertEquals(originalAddr.getEmailAddress(), convertedAddr.getEmailAddress());
    }

    @Test
    void testClientDTOToClientMap() {
        final ClientDTO original = createClientDTO();
        final Client converted = clientMapper.clientDTOToClient(original);

        Assertions.assertEquals(original.getName(), converted.getName());
        Assertions.assertEquals(original.getNif(), converted.getNif());
        Assertions.assertEquals(original.getPhoneNumber(), new BigDecimal(converted.getPhoneNumber()));
        Assertions.assertEquals(original.getPhoneCountryCode().intValue(), converted.getCountryCode().ordinal());

        final AddressDTO originalAddr = original.getAddress();
        final Address convertedAddr = converted.getAddress();

        Assertions.assertEquals(originalAddr.getCountry().intValue(), convertedAddr.getCountry().ordinal());
        Assertions.assertEquals(originalAddr.getCity(), convertedAddr.getCity());
        Assertions.assertEquals(originalAddr.getStreetOne(), convertedAddr.getStreetOne());
        Assertions.assertEquals(originalAddr.getStreetTwo(), convertedAddr.getStreetTwo());
        Assertions.assertEquals(originalAddr.getPostcode(), convertedAddr.getPostcode());
        Assertions.assertEquals(originalAddr.getStateProvince(), convertedAddr.getStateProvince());
        Assertions.assertEquals(originalAddr.getEmailAddress(), convertedAddr.getEmailAddress());
    }
}
