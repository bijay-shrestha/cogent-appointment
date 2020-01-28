package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.model.Doctor;
import com.cogent.cogentappointment.client.repository.custom.DoctorRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 2019-09-29
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, DoctorRepositoryCustom {

    @Query("SELECT d FROM Doctor d WHERE d.status!='D' AND d.id = :id")
    Optional<Doctor> findDoctorById(@Param("id") Long id);

    @Query("SELECT d FROM Doctor d WHERE d.status='Y' AND d.id = :id")
    Optional<Doctor> findActiveDoctorById(@Param("id") Long id);
}
