package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    // ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "countryCode", source = "clientDTO.phoneCountryCode")
    Client clientDTOToClient(ClientDTO clientDTO);

    @Mapping(target = "phoneCountryCode", source = "client.countryCode")
    ClientDTO clientToClientDTO(Client client);

    default CountryCode map(BigDecimal value) {
        final int i = value.intValue();
        return CountryCode.values()[i];
    }

    default BigDecimal map(CountryCode value) {
        return new BigDecimal(value.ordinal());
    }
}
