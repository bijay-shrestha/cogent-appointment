package com.cogent.cogentappointment.repository;

import com.cogent.cogentappointment.model.Patient;
import com.cogent.cogentappointment.repository.custom.PatientRepositoryCustom;
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

    @Query("SELECT COUNT(p.id) FROM Patient p WHERE p.code=:code AND p.status !='D'")
    Long fetchPatientCountByCode(@Param("code") String code);

    @Query("SELECT p FROM Patient p WHERE p.id=:id AND p.status !='D'")
    Optional<Patient> fetchPatientById(@Param("id") Long id);

    @Query(value = "SELECT COUNT(p.id) FROM Patient p WHERE p.id != :id AND p.code= :code And p.status !='D'")
    Long checkPatientCodeIfExist(@Param("id") Long id, @Param("code") String code);
}
