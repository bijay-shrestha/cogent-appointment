package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.QualificationAliasRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Department;
import com.cogent.cogentappointment.persistence.model.QualificationAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 11/11/2019
 */
@Repository
public interface QualificationAliasRepository extends JpaRepository<QualificationAlias, Long>,
        QualificationAliasRepositoryCustom {

    @Query("SELECT q FROM QualificationAlias q WHERE q.status='Y' AND q.id = :id")
    Optional<QualificationAlias> fetchActiveQualificationAliasById(@Param("id") Long id);
}
