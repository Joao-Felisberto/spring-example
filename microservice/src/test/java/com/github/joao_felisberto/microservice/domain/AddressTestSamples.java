package com.github.joao_felisberto.microservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Address getAddressSample1() {
        return new Address()
            .id(1L)
            .city("city1")
            .postcode("postcode1")
            .stateOrProvince("stateOrProvince1")
            .streetOne("streetOne1")
            .streetTwo("streetTwo1")
            .emailAddress("emailAddress1");
    }

    public static Address getAddressSample2() {
        return new Address()
            .id(2L)
            .city("city2")
            .postcode("postcode2")
            .stateOrProvince("stateOrProvince2")
            .streetOne("streetOne2")
            .streetTwo("streetTwo2")
            .emailAddress("emailAddress2");
    }

    public static Address getAddressRandomSampleGenerator() {
        return new Address()
            .id(longCount.incrementAndGet())
            .city(UUID.randomUUID().toString())
            .postcode(UUID.randomUUID().toString())
            .stateOrProvince(UUID.randomUUID().toString())
            .streetOne(UUID.randomUUID().toString())
            .streetTwo(UUID.randomUUID().toString())
            .emailAddress(UUID.randomUUID().toString());
    }
}
