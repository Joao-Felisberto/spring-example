package com.github.joao_felisberto.microservice.repository;

import com.github.joao_felisberto.microservice.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    /**
     * Finds the client with the provided NIF, if any exist.
     *
     * @param nif the client's NIF
     * @return the client with the specified NIF, if any was found
     */
    Optional<Client> findBynif(String nif);

    /**
     * Finds the clients whose name contains the given string.
     *
     * @param name the string which the names shall contain.
     * @return the list of clients whose names contain the passed string
     */
    List<Client> findAllByNameLike(String name);
}
