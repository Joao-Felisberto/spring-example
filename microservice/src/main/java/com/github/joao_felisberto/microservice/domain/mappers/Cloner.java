package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.control.DeepClone;

/**
 * Interface to deep clone some types
 */
@Mapper(
    mappingControl = DeepClone.class,
    componentModel = "spring"
)
public interface Cloner {

    /**
     * Deep clone a {@link Client}
     *
     * @param client the {@link Client} to clone
     * @return the cloned {@link Client}
     */
    Client clone(Client client);

    /**
     * Deep clone a {@link Address}
     *
     * @param address the {@link Address} to clone
     * @return the cloned {@link Address}
     */
    @Mapping(target = "removeClient", ignore = true)
    Address clone(Address address);

    /**
     * Deep clone a {@link ClientDTO}
     *
     * @param clientDTO the {@link ClientDTO} to clone
     * @return the cloned {@link ClientDTO}
     */
    ClientDTO clone(ClientDTO clientDTO);

    /**
     * Deep clone a {@link AddressDTO}
     *
     * @param addressDTO the {@link AddressDTO} to clone
     * @return the cloned {@link AddressDTO}
     */
    AddressDTO clone(AddressDTO addressDTO);
}
