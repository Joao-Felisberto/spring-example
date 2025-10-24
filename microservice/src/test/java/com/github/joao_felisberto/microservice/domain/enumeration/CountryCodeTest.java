package com.github.joao_felisberto.microservice.domain.enumeration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class CountryCodeTest {

    @Test
    void getCode() {
        final Map<CountryCode, String> codeMap = Map.of(
            CountryCode.UNITED_STATES, "1",
            CountryCode.SPAIN, "350",
            CountryCode.PORTUGAL, "351"
        );

        codeMap.forEach((k, v) -> Assertions.assertEquals(k.getCode(), v));
    }
}
