package com.github.joao_felisberto.microservice.domain;

import static com.github.joao_felisberto.microservice.domain.AddressTestSamples.*;
import static com.github.joao_felisberto.microservice.domain.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.joao_felisberto.microservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = getAddressSample1();
        Address address2 = new Address();
        assertThat(address1).isNotEqualTo(address2);

        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);

        address2 = getAddressSample2();
        assertThat(address1).isNotEqualTo(address2);
    }

    @Test
    void clientTest() {
        Address address = getAddressRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        address.addClient(clientBack);
        assertThat(address.getClients()).containsOnly(clientBack);
        assertThat(clientBack.getAddress()).isEqualTo(address);

        address.removeClient(clientBack);
        assertThat(address.getClients()).doesNotContain(clientBack);
        assertThat(clientBack.getAddress()).isNull();

        address.clients(new HashSet<>(Set.of(clientBack)));
        assertThat(address.getClients()).containsOnly(clientBack);
        assertThat(clientBack.getAddress()).isEqualTo(address);

        address.setClients(new HashSet<>());
        assertThat(address.getClients()).doesNotContain(clientBack);
        assertThat(clientBack.getAddress()).isNull();
    }
}
