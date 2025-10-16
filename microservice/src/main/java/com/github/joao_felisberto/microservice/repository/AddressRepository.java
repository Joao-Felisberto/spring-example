package com.github.joao_felisberto.microservice.repository;

import com.github.joao_felisberto.microservice.domain.Address;
import com.github.joao_felisberto.microservice.domain.enumeration.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    default Optional<Address> findSameAddress(Address address) {
        return findByCityAndCountryAndPostcodeAndStateProvinceAndStreetOneAndStreetTwoAndEmailAddress(
            address.getCity(),
            address.getCountry(),
            address.getPostcode(),
            address.getStateProvince(),
            address.getStreetOne(),
            address.getStreetTwo(),
            address.getEmailAddress()
        );
    }

    Optional<Address> findByCityAndCountryAndPostcodeAndStateProvinceAndStreetOneAndStreetTwoAndEmailAddress(
        String city, CountryCode country, String postcode, String stateProvince, String streetOne,
        String streetTwo, String emailAddress
    );
}
