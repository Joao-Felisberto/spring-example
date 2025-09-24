package com.github.joao_felisberto.microservice.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PhoneNumberTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PhoneNumber getPhoneNumberSample1() {
        return new PhoneNumber().id(1L).number(1L);
    }

    public static PhoneNumber getPhoneNumberSample2() {
        return new PhoneNumber().id(2L).number(2L);
    }

    public static PhoneNumber getPhoneNumberRandomSampleGenerator() {
        return new PhoneNumber().id(longCount.incrementAndGet()).number(longCount.incrementAndGet());
    }
}
