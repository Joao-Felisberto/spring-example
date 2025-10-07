package com.github.joao_felisberto.microservice.web.rest.mappers;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.mappers.AddressMapper;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.joao_felisberto.microservice.TestUtil.createAddress;
import static com.github.joao_felisberto.microservice.TestUtil.createAddressDTO;

class AddressMapperTest {
    @Test
    void testAddressToDTOMap() {
        final Address original = createAddress();
        final AddressDTO converted = AddressMapper.INSTANCE.addressToAddressDTO(original);

        Assertions.assertEquals(original.getCountry().ordinal(), converted.getCountry().intValue());
        Assertions.assertEquals(original.getCity(), converted.getCity());
        Assertions.assertEquals(original.getStreetOne(), converted.getStreetOne());
        Assertions.assertEquals(original.getStreetTwo(), converted.getStreetTwo());
        Assertions.assertEquals(original.getPostcode(), converted.getPostcode());
        Assertions.assertEquals(original.getStateOrProvince(), converted.getStateOrProvince());
        Assertions.assertEquals(original.getEmailAddress(), converted.getEmailAddress());
    }

    @Test
    void testAddressDTOToAddressMap() {
        final AddressDTO original = createAddressDTO();
        final Address converted = AddressMapper.INSTANCE.addressDTOToAddress(original);

        Assertions.assertEquals(original.getCountry().intValue(), converted.getCountry().ordinal());
        Assertions.assertEquals(original.getCity(), converted.getCity());
        Assertions.assertEquals(original.getStreetOne(), converted.getStreetOne());
        Assertions.assertEquals(original.getStreetTwo(), converted.getStreetTwo());
        Assertions.assertEquals(original.getPostcode(), converted.getPostcode());
        Assertions.assertEquals(original.getStateOrProvince(), converted.getStateOrProvince());
        Assertions.assertEquals(original.getEmailAddress(), converted.getEmailAddress());
    }
}
