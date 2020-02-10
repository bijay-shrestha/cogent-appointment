package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 10/02/2020
 */
@Repository
public interface HospitalPatientInfoRepository extends JpaRepository<HospitalPatientInfo, Long> {

//    @Query("FROM HospitalPatientInfo hpi WHERE hpi.patientId=:patientId AND hpi.status!='D")
//    HospitalPatientInfo getByPatientId(@Param("patientId") Long patientId);
}
