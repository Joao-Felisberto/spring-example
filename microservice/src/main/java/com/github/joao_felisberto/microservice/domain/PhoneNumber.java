package com.github.joao_felisberto.microservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PhoneNumber.
 */
@Entity
@Table(name = "phone_number")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhoneNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "country_code", nullable = false)
    private CountryCode countryCode;

    @NotNull
    @Min(value = 0L)
    @Column(name = "number", nullable = false)
    private Long number;

    @JsonIgnoreProperties(value = { "phoneNumber", "address" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "phoneNumber")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PhoneNumber id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CountryCode getCountryCode() {
        return this.countryCode;
    }

    public PhoneNumber countryCode(CountryCode countryCode) {
        this.setCountryCode(countryCode);
        return this;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public Long getNumber() {
        return this.number;
    }

    public PhoneNumber number(Long number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        if (this.client != null) {
            this.client.setPhoneNumber(null);
        }
        if (client != null) {
            client.setPhoneNumber(this);
        }
        this.client = client;
    }

    public PhoneNumber client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneNumber)) {
            return false;
        }
        return getId() != null && getId().equals(((PhoneNumber) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhoneNumber{" +
            "id=" + getId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", number=" + getNumber() +
            "}";
    }
}
