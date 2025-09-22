package com.github.joao_felisberto.exercise.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client().id(1L).name("name1").phone("phone1").nif("nif1").address("address1");
    }

    public static Client getClientSample2() {
        return new Client().id(2L).name("name2").phone("phone2").nif("nif2").address("address2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .nif(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
