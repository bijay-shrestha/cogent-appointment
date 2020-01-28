package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.model.Qualification;
import com.cogent.cogentappointment.admin.repository.custom.QualificationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 11/11/2019
 */
@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long>, QualificationRepositoryCustom {

    @Query("SELECT q FROM Qualification q WHERE q.status!='D' AND q.id = :id")
    Optional<Qualification> findQualificationById(@Param("id") Long id);

    @Query("SELECT q FROM Qualification q WHERE q.status='Y' AND q.id = :id")
    Optional<Qualification> fetchActiveQualificationById(@Param("id") Long id);
}
