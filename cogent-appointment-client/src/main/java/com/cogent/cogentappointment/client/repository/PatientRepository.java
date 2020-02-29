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

    @Query("SELECT p FROM Patient p LEFT JOIN HospitalPatientInfo hp ON hp.patient.id= p.id" +
            " WHERE p.id=:id AND hp.hospital.id =:hospitalId AND hp.status !='D'")
    Optional<Patient> fetchPatientByIdAndHospitalId(@Param("id") Long id,
                                                    @Param("hospitalId") Long hospitalId);

    @Query("SELECT p FROM Patient p WHERE p.id=:id")
    Optional<Patient> fetchPatientById(@Param("id") Long id);

}
