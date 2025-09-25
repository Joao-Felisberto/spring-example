package com.github.joao_felisberto.microservice.domain.enumeration;

import java.math.BigDecimal;

/**
 * The CountryCode enumeration.
 */
public enum CountryCode {
    PORTUGAL("351"),
    SPAIN("350"),
    UNITED_STATES("1");

    private final String value;

    CountryCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CountryCode fromJSONIndex(BigDecimal bigI) {
        final int i = bigI.intValue();
        return values()[i];
    }
}
