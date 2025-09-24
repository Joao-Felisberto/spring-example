package com.github.joao_felisberto.microservice.domain.enumeration;

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
}
