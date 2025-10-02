package com.github.joao_felisberto.microservice;

import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;

import java.math.BigDecimal;

public class TestUtil {
    public static final ClientDTO NULL_CLIENT = new ClientDTO(null, null, null, null, null);

    public static ClientDTO createClientDTO() {
        return new ClientDTO(
            "a",
            "1",
            new AddressDTO(
                "a",
                new BigDecimal(CountryCode.PORTUGAL.ordinal()),
                "a",
                "a",
                "a",
                "a",
                "a@a.pt"
            ),
            new BigDecimal(CountryCode.PORTUGAL.ordinal()),
            new BigDecimal(1)
        );
    }
}
