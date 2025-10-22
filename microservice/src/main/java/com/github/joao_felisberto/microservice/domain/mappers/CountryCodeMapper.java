package com.github.joao_felisberto.microservice.domain.mappers;

import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

/**
 * Interface to convert between BigDecimal and CountryCode
 */
@Mapper(/*componentModel = "spring"*/)
public interface CountryCodeMapper {

    /**
     * Convert a BigDecimal into a CountryCode.
     * The {@code value} is the index in the {@code CountryCode.values()} array.
     * If the number excedes the number of elements in the array, an {@link ArrayIndexOutOfBoundsException} can be
     * thrown. If the number is bigger than the integer limit, only the 32 LSBs are counted,
     * and if it has a decimal part it is discarded (not rounded).
     *
     * @param value The index of the {@link CountryCode}
     * @return The {@link CountryCode} with the corresponding index
     */
    default CountryCode bigDecimalToCountryCode(BigDecimal value) {
        final int i = value.intValue();
        return CountryCode.values()[i];
    }

    /**
     * Find the index of the {@link CountryCode} variant as a BigDecimal to be serialized to and from JSON.
     * The returned {@link BigDecimal} will never exceed the integer bounds, as it is an array index.
     *
     * @param value The {@link CountryCode} variant whose index we want to know
     * @return the index of the {@link CountryCode} variant in a {@link BigDecimal}
     */
    default BigDecimal countryCodeToBigDecimal(CountryCode value) {
        return new BigDecimal(value.ordinal());
    }
}
