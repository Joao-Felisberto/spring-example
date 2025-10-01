package com.github.joao_felisberto.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.service.api.dto.ClientDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "nif", nullable = false)
    private String nif;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "phone_country_code", nullable = false)
    private CountryCode countryCode;

    @NotNull
    @Min(value = 0L)
    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"clients"}, allowSetters = true)
    private Address address;

    public static Client fromDTO(ClientDTO dto) {
        // todo shall this be enforced or can we ignore non-null values?
        // id must be null for the database
        return new Client()
            .name(dto.getName())
            .nif(dto.getNif())
            .phoneNumber(dto.getPhoneNumber().longValue())
            .countryCode(CountryCode.fromBigDecimalIndex(dto.getPhoneCountryCode()))
            .address(Address.fromDTO(dto.getAddress()));
    }

    public ClientDTO toDTO() {
        return new ClientDTO(
            this.name,
            this.nif,
            this.address.toDTO(),
            new BigDecimal(this.phoneNumber),
            new BigDecimal(this.countryCode.ordinal())
        );
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Client name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return this.nif;
    }

    public Client nif(String nif) {
        this.setNif(nif);
        return this;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Client address(Address address) {
        this.setAddress(address);
        return this;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Client phoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Client countryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return getId() != null && getId().equals(((Client) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nif='" + getNif() + "'" +
            ", address=" + getAddress() + "'" +
            ", phone_number=" + getPhoneNumber() + "'" +
            "}";
    }
}
