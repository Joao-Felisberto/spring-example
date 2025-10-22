package com.github.joao_felisberto.microservice.config;

import com.github.joao_felisberto.microservice.domain.mappers.AddressMapper;
import com.github.joao_felisberto.microservice.domain.mappers.ClientMapper;
import com.github.joao_felisberto.microservice.domain.mappers.CountryCodeMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public ClientMapper clientMapper() {
        return Mappers.getMapper(ClientMapper.class);
    }

    @Bean
    public AddressMapper addressMapper() {
        return Mappers.getMapper(AddressMapper.class);
    }

    @Bean
    public CountryCodeMapper countryCodeMapper() {
        return Mappers.getMapper(CountryCodeMapper.class);
    }

}
