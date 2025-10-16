package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.control.DeepClone;

@Mapper(
    mappingControl = DeepClone.class,
    componentModel = "spring"
)
public interface Cloner {

    Client clone(Client client);

    @Mapping(target = "removeClient", ignore = true)
    Address clone(Address address);

    ClientDTO clone(ClientDTO clientDTO);

    AddressDTO clone(AddressDTO addressDTO);
}
