package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {

    @Query("SELECT p FROM Patient p LEFT JOIN HospitalPatientInfo hp ON hp.patientId= p.id" +
            " WHERE p.id=:id AND hp.status !='D'")
    Optional<Patient> fetchPatientById(@Param("id") Long id);

    @Query("SELECT p FROM Patient p LEFT JOIN HospitalPatientInfo hp ON hp.patientId= p.id" +
            " WHERE p.id=:id AND hp.status ='Y'")
    Optional<Patient> fetchActivePatientById(@Param("id") Long id);

}
