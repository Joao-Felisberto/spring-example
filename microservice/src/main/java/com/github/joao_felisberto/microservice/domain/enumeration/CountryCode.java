package com.github.joao_felisberto.microservice.domain.enumeration;

/**
 * The enumeration of all available country codes.
 * The country codes are stored as just the code, no '+' sign.
 *
 */
public enum CountryCode {
    PORTUGAL("351"),
    SPAIN("350"),
    UNITED_STATES("1");

    private final String code;

    CountryCode(String code) {
        this.code = code;
    }

    /**
     * Getter for the country code
     *
     * @return the country code
     */
    public String getCode() {
        return code;
    }
}
