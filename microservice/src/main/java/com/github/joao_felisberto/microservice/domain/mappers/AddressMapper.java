package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interface to convert between {@link Address} and {@link AddressDTO}
 */
@Mapper(uses = CountryCodeMapper.class)
public interface AddressMapper {

    /**
     * Convert an {@link Address} into an {@link AddressDTO}.
     * The resulting DTO has no id or clients field.
     * The id will be {@code null} and not the one in the database, if any exists.
     *
     * @param addressDTO The source {@link AddressDTO}
     * @return the resulting {@link Address}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clients", ignore = true)
    @Mapping(target = "removeClient", ignore = true)
    Address addressDTOToAddress(AddressDTO addressDTO);

    /**
     * Convert an {@link AddressDTO} into an {@link Address}.
     *
     * @param address The source {@link Address}
     * @return the resulting {@link AddressDTO}
     */
    AddressDTO addressToAddressDTO(Address address);
}
