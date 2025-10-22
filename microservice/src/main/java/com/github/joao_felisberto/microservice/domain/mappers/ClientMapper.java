package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface to convert between {@link Client} and {@link ClientDTO}
 */
@Mapper(uses = {CountryCodeMapper.class, AddressMapper.class})
public interface ClientMapper {

    /**
     * Convert an {@link Client} into an {@link ClientDTO}.
     * The resulting DTO has no id field.
     * The id will be {@code null} and not the one in the database, if any exists.
     *
     * @param clientDTO The source {@link ClientDTO}
     * @return the resulting {@link Client}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "countryCode", source = "clientDTO.phoneCountryCode")
    Client clientDTOToClient(ClientDTO clientDTO);

    /**
     * Convert an {@link ClientDTO} into an {@link Client}.
     *
     * @param client The source {@link Client}
     * @return the resulting {@link ClientDTO}
     */
    @Mapping(target = "phoneCountryCode", source = "client.countryCode")
    ClientDTO clientToClientDTO(Client client);
}
