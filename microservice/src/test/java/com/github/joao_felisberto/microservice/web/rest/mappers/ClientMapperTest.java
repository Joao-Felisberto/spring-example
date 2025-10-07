package com.github.joao_felisberto.microservice.web.rest.mappers;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.github.joao_felisberto.microservice.TestUtil.createClient;
import static com.github.joao_felisberto.microservice.TestUtil.createClientDTO;

class ClientMapperTest {

    @Test
    void testClientToDTOMap() {
        final Client original = createClient();
        final ClientDTO converted = ClientMapper.INSTANCE.clientToClientDTO(original);

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
        Assertions.assertEquals(originalAddr.getStateOrProvince(), convertedAddr.getStateOrProvince());
        Assertions.assertEquals(originalAddr.getEmailAddress(), convertedAddr.getEmailAddress());
    }

    @Test
    void testCountryCodeToBigDecimal() {
        final CountryCode[] countries = CountryCode.values();

        for (CountryCode c : countries) {
            final BigDecimal converted = ClientMapper.INSTANCE.map(c);
            Assertions.assertEquals(converted.intValue(), c.ordinal());
        }
    }

    @Test
    void testClientDTOToClientMap() {
        final ClientDTO original = createClientDTO();
        final Client converted = ClientMapper.INSTANCE.clientDTOToClient(original);

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
        Assertions.assertEquals(originalAddr.getStateOrProvince(), convertedAddr.getStateOrProvince());
        Assertions.assertEquals(originalAddr.getEmailAddress(), convertedAddr.getEmailAddress());
    }

    @Test
    void testBigDecimalToCountryCode() {
        final BigDecimal max = BigDecimal.valueOf(CountryCode.values().length);

        for (BigDecimal i = new BigDecimal(0); i.compareTo(max) < 0; i = i.add(new BigDecimal(1))) {
            final CountryCode converted = ClientMapper.INSTANCE.map(i);
            Assertions.assertEquals(i.intValue(), converted.ordinal());
        }
    }
}
