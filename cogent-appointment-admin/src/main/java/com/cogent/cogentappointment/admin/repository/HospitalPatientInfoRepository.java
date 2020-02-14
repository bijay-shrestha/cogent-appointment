package com.cogent.cogentappointment.admin.repository;

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
public interface HospitalPatientInfoRepository extends JpaRepository<HospitalPatientInfo, Long> {

    @Query("SELECT hpi FROM HospitalPatientInfo hpi WHERE hpi.patientId=:patientId AND hpi.status!='D'")
    HospitalPatientInfo fetchHospitalPatientInfoByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT h FROM HospitalPatientInfo h WHERE h.patientId=:patientId AND h.status ='Y'")
    Optional<HospitalPatientInfo> findByPatientId(@Param("patientId") Long patientId);
}
