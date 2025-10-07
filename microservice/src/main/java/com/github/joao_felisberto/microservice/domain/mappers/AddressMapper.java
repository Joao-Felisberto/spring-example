package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "removeClient", ignore = true)
    Address addressDTOToAddress(AddressDTO addressDTO);

    AddressDTO addressToAddressDTO(Address address);

    default CountryCode map(BigDecimal value) {
        final int i = value.intValue();
        return CountryCode.values()[i];
    }

    default BigDecimal map(CountryCode value) {
        return new BigDecimal(value.ordinal());
    }
}
