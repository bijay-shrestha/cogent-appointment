package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.model.Country;
import com.cogent.cogentappointment.client.repository.custom.CountryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 08/11/2019
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long>, CountryRepositoryCustom {

    @Query("SELECT c FROM Country c WHERE c.status='Y' AND c.id = :id")
    Optional<Country> fetchActiveCountryById(@Param("id") Long id);
}
