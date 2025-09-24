package com.github.joao_felisberto.microservice.repository;

import com.github.joao_felisberto.microservice.domain.PhoneNumber;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PhoneNumber entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {}
