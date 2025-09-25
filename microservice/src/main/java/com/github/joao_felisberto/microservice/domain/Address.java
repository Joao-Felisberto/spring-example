package com.github.joao_felisberto.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import com.github.joao_felisberto.microservice.service.api.dto.AddressDTO;
import com.github.joao_felisberto.microservice.service.api.dto.PhoneNumberDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country", nullable = false)
    private CountryCode country;

    @NotNull
    @Column(name = "postcode", nullable = false)
    private String postcode;

    @NotNull
    @Column(name = "state_or_province", nullable = false)
    private String stateOrProvince;

    @NotNull
    @Column(name = "street_one", nullable = false)
    private String streetOne;

    @NotNull
    @Column(name = "street_two", nullable = false)
    private String streetTwo;

    @NotNull
    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "phoneNumber", "address" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    public static Address fromDTO(AddressDTO dto) {
        return new Address()
            .city(dto.getCity())
            .country(CountryCode.fromJSONIndex(dto.getCountry()))
            .postcode(dto.getPostcode())
            .stateOrProvince(dto.getStateOrProvince())
            .streetOne(dto.getStreetOne())
            .streetTwo(dto.getStreetTwo())
            .emailAddress(dto.getEmailAddress());
    }

    public AddressDTO toDTO() {
        return new AddressDTO(
            this.city,
            new BigDecimal(this.country.ordinal()),
            this.postcode,
            this.stateOrProvince,
            this.streetOne,
            this.streetTwo,
            this.emailAddress
        );
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Address id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return this.city;
    }

    public Address city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CountryCode getCountry() {
        return this.country;
    }

    public Address country(CountryCode country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(CountryCode country) {
        this.country = country;
    }

    public String getPostcode() {
        return this.postcode;
    }

    public Address postcode(String postcode) {
        this.setPostcode(postcode);
        return this;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStateOrProvince() {
        return this.stateOrProvince;
    }

    public Address stateOrProvince(String stateOrProvince) {
        this.setStateOrProvince(stateOrProvince);
        return this;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getStreetOne() {
        return this.streetOne;
    }

    public Address streetOne(String streetOne) {
        this.setStreetOne(streetOne);
        return this;
    }

    public void setStreetOne(String streetOne) {
        this.streetOne = streetOne;
    }

    public String getStreetTwo() {
        return this.streetTwo;
    }

    public Address streetTwo(String streetTwo) {
        this.setStreetTwo(streetTwo);
        return this;
    }

    public void setStreetTwo(String streetTwo) {
        this.streetTwo = streetTwo;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Address emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setAddress(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setAddress(this));
        }
        this.clients = clients;
    }

    public Address clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Address addClient(Client client) {
        this.clients.add(client);
        client.setAddress(this);
        return this;
    }

    public Address removeClient(Client client) {
        this.clients.remove(client);
        client.setAddress(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return getId() != null && getId().equals(((Address) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", postcode='" + getPostcode() + "'" +
            ", stateOrProvince='" + getStateOrProvince() + "'" +
            ", streetOne='" + getStreetOne() + "'" +
            ", streetTwo='" + getStreetTwo() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            "}";
    }
}
