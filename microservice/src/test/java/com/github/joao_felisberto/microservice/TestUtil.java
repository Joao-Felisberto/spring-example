package com.github.joao_felisberto.microservice;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.Client;
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

    public static ClientDTO createDistinctClientDTO(String string) {
        return new ClientDTO(
            string,
            "1",
            new AddressDTO(
                string,
                new BigDecimal(CountryCode.PORTUGAL.ordinal()),
                string,
                string,
                string,
                string,
                String.format("%s@%s.pt", string, string)
            ),
            new BigDecimal(CountryCode.PORTUGAL.ordinal()),
            new BigDecimal(1)
        );
    }

    public static AddressDTO createAddressDTO() {
        return new AddressDTO(
            "a",
            new BigDecimal(CountryCode.PORTUGAL.ordinal()),
            "a",
            "a",
            "a",
            "a",
            "a@a.pt"
        );
    }

    public static Client createClient() {
        return new Client()
            .name("a")
            .nif("1")
            .countryCode(CountryCode.PORTUGAL)
            .phoneNumber(1L)
            .address(createAddress());
    }

    public static Address createAddress() {
        return new Address()
            .city("a")
            .country(CountryCode.PORTUGAL)
            .postcode("a")
            .stateOrProvince("a")
            .streetOne("a")
            .streetTwo("a")
            .emailAddress("a");
    }
}
