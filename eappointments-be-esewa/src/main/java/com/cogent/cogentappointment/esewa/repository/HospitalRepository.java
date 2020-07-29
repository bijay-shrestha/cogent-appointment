package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalRepositoryCustom;
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

    @Query("SELECT h FROM Hospital h WHERE h.status='Y' AND h.id = :id")
    Optional<Hospital> findActiveHospitalById(@Param("id") Long id);

    @Query("SELECT h.refundPercentage FROM Hospital h WHERE h.status='Y' AND h.id = :id")
    Optional<Double> fetchHospitalRefundPercentage(@Param("id") Long id);
}


