package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.HospitalRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 12/01/2020
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>, HospitalRepositoryCustom {

    @Query("SELECT h FROM Hospital h WHERE h.status!='D' AND h.id = :id AND h.isCompany='N'")
    Optional<Hospital> findHospitalById(@Param("id") Long id);

    @Query("SELECT h FROM Hospital h WHERE h.status!='D' AND h.isCompany='Y' AND h.id = :id")
    Optional<Hospital> findCompanyById(@Param("id") Long id);

    @Query("SELECT h FROM Hospital h WHERE h.status='Y' AND h.id = :id AND h.isCompany='N'")
    Optional<Hospital> findActiveHospitalById(@Param("id") Long id);

    @Query("SELECT h FROM Hospital h WHERE h.status='Y' AND h.id = :id AND h.isCompany='Y'")
    Optional<Hospital> findActiveCompanyById(@Param("id") Long id);

    @Query("SELECT h.alias FROM Hospital h WHERE h.status='Y' AND h.id = :id")
    Optional<String> fetchAliasById(@Param("id") Long id);

}


