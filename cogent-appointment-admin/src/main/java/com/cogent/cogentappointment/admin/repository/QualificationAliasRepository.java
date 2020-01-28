package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.model.QualificationAlias;
import com.cogent.cogentappointment.admin.repository.custom.QualificationAliasRepositoryCustom;
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
