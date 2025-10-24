package com.github.joao_felisberto.microservice.service.criteria;

import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class ClientCriteriaTest {

    @Test
    void newClientCriteriaHasAllFiltersNullTest() {
        var clientCriteria = new ClientCriteria();
        assertThat(clientCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clientCriteriaFluentMethodsCreatesFiltersTest() {
        var clientCriteria = new ClientCriteria();

        setAllFilters(clientCriteria);

        assertThat(clientCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clientCriteriaCopyCreatesNullFilterTest() {
        var clientCriteria = new ClientCriteria();
        var copy = clientCriteria.copy();

        assertThat(clientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clientCriteria)
        );
    }

    @Test
    void clientCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clientCriteria = new ClientCriteria();
        setAllFilters(clientCriteria);

        var copy = clientCriteria.copy();

        assertThat(clientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clientCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clientCriteria = new ClientCriteria();

        assertThat(clientCriteria).hasToString("ClientCriteria{}");
    }

    @Test
    void equalsVerifier() {
        final ClientCriteria clientCriteria = new ClientCriteria();

        Assertions.assertTrue(clientCriteria.equals(clientCriteria));
        Assertions.assertFalse(clientCriteria.equals(null));
        Assertions.assertFalse(clientCriteria.equals("I am not a ClientCriteria!"));

        final ClientCriteriaBuilder other = new ClientCriteriaBuilder(clientCriteria);

        Assertions.assertFalse(clientCriteria.equals(other.distinct(false).get()));
        Assertions.assertFalse(clientCriteria.equals(other.isAddress(2L).get()));
        Assertions.assertFalse(clientCriteria.equals(other.isPhone(2L).get()));
        Assertions.assertFalse(clientCriteria.equals(other.isCountry(CountryCode.UNITED_STATES).get()));
        Assertions.assertFalse(clientCriteria.equals(other.isNIF("DIFFERENT").get()));
        Assertions.assertFalse(clientCriteria.equals(other.isName("DIFFERENT").get()));
        Assertions.assertFalse(clientCriteria.equals(other.isID(2L).get()));
    }

    private static void setAllFilters(ClientCriteria clientCriteria) {
        clientCriteria.setId(new LongFilter());
        clientCriteria.setName(new StringFilter());
        clientCriteria.setNif(new StringFilter());
        clientCriteria.setCountryCode(new ClientCriteria.CountryCodeFilter());
        clientCriteria.setPhoneNumber(new LongFilter());
        clientCriteria.setAddressId(new LongFilter());
        clientCriteria.setDistinct(true);
    }

    private static Condition<ClientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                    condition.apply(criteria.getName()) &&
                    condition.apply(criteria.getNif()) &&
                    condition.apply(criteria.getCountryCode()) &&
                    condition.apply(criteria.getPhoneNumber()) &&
                    condition.apply(criteria.getAddressId()) &&
                    condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClientCriteria> copyFiltersAre(ClientCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                    condition.apply(criteria.getName(), copy.getName()) &&
                    condition.apply(criteria.getNif(), copy.getNif()) &&
                    condition.apply(criteria.getCountryCode(), copy.getCountryCode()) &&
                    condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                    condition.apply(criteria.getAddressId(), copy.getAddressId()) &&
                    condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }

    private class ClientCriteriaBuilder {
        private ClientCriteria clientCriteria;

        public ClientCriteriaBuilder() {
            clientCriteria = new ClientCriteria();
        }

        public ClientCriteriaBuilder(ClientCriteria clientCriteria) {
            this.clientCriteria = new ClientCriteria(clientCriteria);
        }

        public ClientCriteria get() {
            return this.clientCriteria;
        }

        public ClientCriteriaBuilder isID(Long id) {
            this.clientCriteria.setId(
                (LongFilter) new LongFilter()
                    .setEquals(id)
            );
            return this;
        }

        public ClientCriteriaBuilder isName(String name) {
            this.clientCriteria.setName(
                (StringFilter) new StringFilter()
                    .setEquals(name)
            );
            return this;
        }

        public ClientCriteriaBuilder isNIF(String nif) {
            this.clientCriteria.setNif(
                (StringFilter) new StringFilter()
                    .setEquals(nif)
            );
            return this;
        }

        public ClientCriteriaBuilder isCountry(CountryCode countryCode) {
            this.clientCriteria.setCountryCode(
                (ClientCriteria.CountryCodeFilter) new ClientCriteria.CountryCodeFilter()
                    .setEquals(countryCode)
            );
            return this;
        }

        public ClientCriteriaBuilder isPhone(Long phone) {
            this.clientCriteria.setPhoneNumber(
                (LongFilter) new LongFilter()
                    .setEquals(phone)
            );
            return this;
        }

        public ClientCriteriaBuilder isAddress(Long id) {
            this.clientCriteria.setAddressId(
                (LongFilter) new LongFilter()
                    .setEquals(id)
            );
            return this;
        }

        public ClientCriteriaBuilder distinct(Boolean distinct) {
            this.clientCriteria.setDistinct(distinct);
            return this;
        }
    }
}
