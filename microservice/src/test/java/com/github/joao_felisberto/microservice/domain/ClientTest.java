package com.github.joao_felisberto.microservice.domain;

import static com.github.joao_felisberto.microservice.domain.AddressTestSamples.*;
import static com.github.joao_felisberto.microservice.domain.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.joao_felisberto.microservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

//    @Test
//    void phoneNumberTest() {
//        Client client = getClientRandomSampleGenerator();
//        PhoneNumber phoneNumberBack = getPhoneNumberRandomSampleGenerator();
//
//        client.setPhoneNumber(phoneNumberBack);
//        assertThat(client.getPhoneNumber()).isEqualTo(phoneNumberBack);
//
//        client.phoneNumber(null);
//        assertThat(client.getPhoneNumber()).isNull();
//    }

    @Test
    void addressTest() {
        Client client = getClientRandomSampleGenerator();
        Address addressBack = getAddressRandomSampleGenerator();

        client.setAddress(addressBack);
        assertThat(client.getAddress()).isEqualTo(addressBack);

        client.address(null);
        assertThat(client.getAddress()).isNull();
    }
}
