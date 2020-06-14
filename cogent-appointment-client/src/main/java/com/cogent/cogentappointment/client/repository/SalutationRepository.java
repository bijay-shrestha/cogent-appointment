package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.SalutationRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Salutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalutationRepository extends JpaRepository<Salutation, Long>, SalutationRepositoryCustom {

    @Query("SELECT s FROM Salutation s WHERE s.status='Y' AND s.id = :id")
    Optional<Salutation> fetchActiveSalutationById(@Param("id") Long id);

}
