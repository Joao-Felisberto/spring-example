package com.github.joao_felisberto.exercise.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client().id(1L).name("name1").nif(1).address("address1").phone(1);
    }

    public static Client getClientSample2() {
        return new Client().id(2L).name("name2").nif(2).address("address2").phone(2);
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .nif(intCount.incrementAndGet())
            .address(UUID.randomUUID().toString())
            .phone(intCount.incrementAndGet());
    }
}
