package com.github.joao_felisberto.microservice.service.criteria;

import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Criteria class for the {@link com.github.joao_felisberto.microservice.domain.Client} entity. This class is used
 * in {@link com.github.joao_felisberto.microservice.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CountryCode
     */
    public static class CountryCodeFilter extends Filter<CountryCode> {

        @Serial
        private static final long serialVersionUID = 3017340447575840393L;

        public CountryCodeFilter() {
        }

        public CountryCodeFilter(CountryCodeFilter filter) {
            super(filter);
        }

        @Override
        public CountryCodeFilter copy() {
            return new CountryCodeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter nif;

    private CountryCodeFilter countryCode;

    private LongFilter phoneNumber;

    private LongFilter addressId;

    private Boolean distinct;

    public ClientCriteria() {
    }

    public ClientCriteria(ClientCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.nif = other.optionalNif().map(StringFilter::copy).orElse(null);
        this.countryCode = other.optionalCountryCode().map(CountryCodeFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(LongFilter::copy).orElse(null);
        this.addressId = other.optionalAddressId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getNif() {
        return nif;
    }

    public Optional<StringFilter> optionalNif() {
        return Optional.ofNullable(nif);
    }

    public void setNif(StringFilter nif) {
        this.nif = nif;
    }

    public CountryCodeFilter getCountryCode() {
        return countryCode;
    }

    public Optional<CountryCodeFilter> optionalCountryCode() {
        return Optional.ofNullable(countryCode);
    }

    public void setCountryCode(CountryCodeFilter countryCode) {
        this.countryCode = countryCode;
    }

    public LongFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<LongFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public void setPhoneNumber(LongFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public Optional<LongFilter> optionalAddressId() {
        return Optional.ofNullable(addressId);
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClientCriteria that = (ClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(nif, that.nif) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(addressId, that.addressId) &&
                Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nif, countryCode, phoneNumber, addressId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalNif().map(f -> "nif=" + f + ", ").orElse("") +
            optionalCountryCode().map(f -> "countryCode=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalAddressId().map(f -> "addressId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
            "}";
    }
}
