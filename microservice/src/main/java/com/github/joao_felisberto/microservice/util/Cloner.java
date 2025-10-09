package com.github.joao_felisberto.microservice.util;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

@Mapper(mappingControl = DeepClone.class)
public interface Cloner {
    Cloner INSTANCE = Mappers.getMapper(Cloner.class);

    Client clone(Client client);

    Address clone(Address address);

    ClientDTO clone(ClientDTO clientDTO);

    AddressDTO clone(AddressDTO addressDTO);
}
