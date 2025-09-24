package com.github.joao_felisberto.microservice.domain;

import static com.github.joao_felisberto.microservice.domain.ClientTestSamples.*;
import static com.github.joao_felisberto.microservice.domain.PhoneNumberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.joao_felisberto.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhoneNumberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhoneNumber.class);
        PhoneNumber phoneNumber1 = getPhoneNumberSample1();
        PhoneNumber phoneNumber2 = new PhoneNumber();
        assertThat(phoneNumber1).isNotEqualTo(phoneNumber2);

        phoneNumber2.setId(phoneNumber1.getId());
        assertThat(phoneNumber1).isEqualTo(phoneNumber2);

        phoneNumber2 = getPhoneNumberSample2();
        assertThat(phoneNumber1).isNotEqualTo(phoneNumber2);
    }

    @Test
    void clientTest() {
        PhoneNumber phoneNumber = getPhoneNumberRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        phoneNumber.setClient(clientBack);
        assertThat(phoneNumber.getClient()).isEqualTo(clientBack);
        assertThat(clientBack.getPhoneNumber()).isEqualTo(phoneNumber);

        phoneNumber.client(null);
        assertThat(phoneNumber.getClient()).isNull();
        assertThat(clientBack.getPhoneNumber()).isNull();
    }
}
