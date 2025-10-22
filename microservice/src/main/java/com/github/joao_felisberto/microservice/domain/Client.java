package com.github.joao_felisberto.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * Holds fiscal information about a Client, namely their fiscal identifier, name, phone number and address.
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"clients"}, allowSetters = true)
    private Address address;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    /**
     * Getter for the id
     *
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Fluent setter for the id
     *
     * @param id the id
     * @return this client after the change
     */
    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    /**
     * Setter for the id
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the name
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Fluent setter for the name
     *
     * @param name the name
     * @return this client after the change
     */
    public Client name(String name) {
        this.setName(name);
        return this;
    }

    /**
     * Setter for the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the fiscal identifier
     *
     * @return the fiscal identifier
     */
    public String getNif() {
        return this.nif;
    }

    /**
     * Fluent setter for the nif
     *
     * @param nif the nif
     * @return this client after the change
     */
    public Client nif(String nif) {
        this.setNif(nif);
        return this;
    }

    /**
     * Setter for the fiscal identifier
     *
     * @param nif the fiscal identifier
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * Getter for the address
     *
     * @return the address
     */
    public Address getAddress() {
        return this.address;
    }

    /**
     * Setter for the address
     *
     * @param address the address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Fluent setter for the address
     *
     * @param address the address
     * @return this client after the change
     */
    public Client address(Address address) {
        this.setAddress(address);
        return this;
    }

    /**
     * Getter for the client's phone number's country code
     *
     * @return the client's phone number's country code
     */
    public CountryCode getCountryCode() {
        return countryCode;
    }

    /**
     * Setter for the client's phone number's country code
     *
     * @param countryCode the client's phone number's country code
     */
    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Getter for the phone number
     *
     * @return the phone number
     */
    public Long getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Setter for the phone number
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Fluent setter for the phone number
     *
     * @param phoneNumber the phone number
     * @return this client after the change
     */
    public Client phoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    /**
     * Fluent setter for the country code
     *
     * @param countryCode country code
     * @return this client after the change
     */
    public Client countryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    /**
     * Two Clients are equal if and only if they have the same id and it is not null
     *
     * @param o the reference client with which to compare.
     * @return true if the clients are equal
     */
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
