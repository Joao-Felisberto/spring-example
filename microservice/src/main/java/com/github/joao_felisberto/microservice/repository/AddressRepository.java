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

    /**
     * Finds an address equal to the argument in every field except the id
     *
     * @param address The address to find
     * @return the found address, if any was found, in an {@link Optional}
     */
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

    /**
     * Finds an address with the passed fields
     *
     * @param city          the address' city
     * @param country       the address' city
     * @param postcode      the address' city
     * @param stateProvince the address' city
     * @param streetOne     the address' city
     * @param streetTwo     the address' city
     * @param emailAddress  the address' city
     * @return the found address, if any was found, in an {@link Optional}
     */
    Optional<Address> findByCityAndCountryAndPostcodeAndStateProvinceAndStreetOneAndStreetTwoAndEmailAddress(
        String city, CountryCode country, String postcode, String stateProvince, String streetOne,
        String streetTwo, String emailAddress
    );
}
