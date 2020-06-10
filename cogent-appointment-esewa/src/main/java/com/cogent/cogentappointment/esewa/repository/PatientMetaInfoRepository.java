package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.PatientMetaInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-08-27
 */
@Repository
public interface PatientMetaInfoRepository extends JpaRepository<PatientMetaInfo, Long> {

    @Query("SELECT pmi FROM PatientMetaInfo pmi WHERE pmi.patient.id=:patientId AND pmi.status!='D'")
    PatientMetaInfo fetchByPatientId(@Param("patientId") Long patientId);
}
