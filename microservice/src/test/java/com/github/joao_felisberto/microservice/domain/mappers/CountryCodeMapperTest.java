package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

class CountryCodeMapperTest {

    private final CountryCodeMapper countryCodeMapper = Mappers.getMapper(CountryCodeMapper.class);

    @Test
    void testCountryCodeToBigDecimal() {
        final CountryCode[] countries = CountryCode.values();

        for (CountryCode c : countries) {
            final BigDecimal converted = countryCodeMapper.countryCodeToBigDecimal(c);
            Assertions.assertEquals(converted.intValue(), c.ordinal());
        }
    }


    @Test
    void testBigDecimalToCountryCode() {
        final BigDecimal max = BigDecimal.valueOf(CountryCode.values().length);

        for (BigDecimal i = new BigDecimal(0); i.compareTo(max) < 0; i = i.add(new BigDecimal(1))) {
            final CountryCode converted = countryCodeMapper.bigDecimalToCountryCode(i);
            Assertions.assertEquals(i.intValue(), converted.ordinal());
        }
    }
}
