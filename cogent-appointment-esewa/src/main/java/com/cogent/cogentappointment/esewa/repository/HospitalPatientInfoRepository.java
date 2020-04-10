package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalPatientInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 10/02/2020
 */
@Repository
public interface HospitalPatientInfoRepository extends JpaRepository<HospitalPatientInfo, Long>,
        HospitalPatientInfoRepositoryCustom {

    @Query("SELECT hpi FROM HospitalPatientInfo hpi WHERE hpi.id=:id")
    Optional<HospitalPatientInfo> fetchHospitalPatientInfoById(@Param("id") Long id);
}
