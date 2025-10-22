package com.github.joao_felisberto.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * An Address.
 * Holds information pertaining to a physical address as defined by
 * <a href=https://github.com/tmforum-apis/TMF632_PartyManagement/blob/master/Party_Management.regular.swagger.json>Party Management</a>
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
    private String stateProvince;

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
    @JsonIgnoreProperties(value = {"phoneNumber", "address"}, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    /**
     * Getter for the ID
     *
     * @return the ID
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Fluent setter for the id
     *
     * @param id the id
     * @return this address after the change
     */
    public Address id(Long id) {
        this.setId(id);
        return this;
    }

    /**
     * Setter for the ID
     *
     * @param id the ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for the city
     *
     * @return the city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Fluent setter for the city
     *
     * @param city the city
     * @return this address after the change
     */
    public Address city(String city) {
        this.setCity(city);
        return this;
    }

    /**
     * Setter for the city
     *
     * @param city the city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * getter for the country
     *
     * @return the country
     */
    public CountryCode getCountry() {
        return this.country;
    }

    /**
     * Fluent setter for the country
     *
     * @param country the country
     * @return this address after the change
     */
    public Address country(CountryCode country) {
        this.setCountry(country);
        return this;
    }

    /**
     * setter for the country
     *
     * @param country the country
     */
    public void setCountry(CountryCode country) {
        this.country = country;
    }

    /**
     * getter for the postcode
     *
     * @return the postcode
     */
    public String getPostcode() {
        return this.postcode;
    }

    /**
     * Fluent setter for the postcode
     *
     * @param postcode the postcode
     * @return this address after the change
     */
    public Address postcode(String postcode) {
        this.setPostcode(postcode);
        return this;
    }

    /**
     * Setter for the postcode
     *
     * @param postcode the postcode
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * Getter for the state or province
     *
     * @return the state or province
     */
    public String getStateProvince() {
        return this.stateProvince;
    }

    /**
     * Fluent setter for the state or province
     *
     * @param stateProvince the state or province
     * @return this address after the change
     */
    public Address stateProvince(String stateProvince) {
        this.setStateProvince(stateProvince);
        return this;
    }

    /**
     * Setter for the state or province
     *
     * @param stateProvince the state or province
     */
    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    /**
     * Getter for the first street field
     *
     * @return the first street field
     */
    public String getStreetOne() {
        return this.streetOne;
    }

    /**
     * Fluent setter for the first street field
     *
     * @param streetOne the first street field
     * @return this address after the change
     */
    public Address streetOne(String streetOne) {
        this.setStreetOne(streetOne);
        return this;
    }

    /**
     * Setter for the first street field
     *
     * @param streetOne the first street field
     */
    public void setStreetOne(String streetOne) {
        this.streetOne = streetOne;
    }

    /**
     * Getter for the second street field
     *
     * @return the second street field
     */
    public String getStreetTwo() {
        return this.streetTwo;
    }

    /**
     * Fluent setter for the second street field
     *
     * @param streetTwo the second street field
     * @return this address after the change
     */
    public Address streetTwo(String streetTwo) {
        this.setStreetTwo(streetTwo);
        return this;
    }

    /**
     * Setter for the second street field
     *
     * @param streetTwo the second street field
     */
    public void setStreetTwo(String streetTwo) {
        this.streetTwo = streetTwo;
    }

    /**
     * Getter for the email
     *
     * @return the email
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * Fluent setter for the email
     *
     * @param emailAddress the email
     * @return this address after the change
     */
    public Address emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    /**
     * setter for the email
     *
     * @param emailAddress the email
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Getter for the clients with this address
     *
     * @return the client set
     */
    public Set<Client> getClients() {
        return this.clients;
    }

    /**
     * Setter for the set of clients with this address.
     * If any client in the set has an address already, it is set to this.
     *
     * @param clients the client set
     */
    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setAddress(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setAddress(this));
        }
        this.clients = clients;
    }

    /**
     * Fluent setter for the client set
     *
     * @param clients the set of clients with this address
     * @return this address after the change
     */
    public Address clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    /**
     * Add a client to the client set
     *
     * @param client The client to add
     * @return this address
     */
    public Address addClient(Client client) {
        this.clients.add(client);
        client.setAddress(this);
        return this;
    }

    /**
     * Remove a client from the client set
     *
     * @param client The client to remove
     * @return this address
     */
    public Address removeClient(Client client) {
        this.clients.remove(client);
        client.setAddress(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    /**
     * Two Addresses are equal if and only if they have the same id and it is not null
     *
     * @param o the reference address with which to compare.
     * @return true if the addresses are equal
     */
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
            ", stateProvince='" + getStateProvince() + "'" +
            ", streetOne='" + getStreetOne() + "'" +
            ", streetTwo='" + getStreetTwo() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            "}";
    }
}
