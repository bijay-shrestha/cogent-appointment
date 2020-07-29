package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.PatientRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 16/01/2020
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, PatientRepositoryCustom {



}
