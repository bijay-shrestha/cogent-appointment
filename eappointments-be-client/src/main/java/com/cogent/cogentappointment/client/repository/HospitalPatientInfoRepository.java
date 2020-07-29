package com.cogent.cogentappointment.client.repository;

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
public interface HospitalPatientInfoRepository extends JpaRepository<HospitalPatientInfo, Long>{

    @Query("SELECT hpi FROM HospitalPatientInfo hpi WHERE hpi.id=:id AND hpi.status!='D'")
    HospitalPatientInfo fetchHospitalPatientInfoByPatientId(@Param("id") Long id);

    @Query("SELECT h FROM HospitalPatientInfo h WHERE h.patient.id=:patientId AND h.hospital.id=:hospitalId")
    Optional<HospitalPatientInfo> findByPatientAndHospitalId(@Param("patientId") Long patientId,
                                                             @Param("hospitalId") Long hospitalId);
}
