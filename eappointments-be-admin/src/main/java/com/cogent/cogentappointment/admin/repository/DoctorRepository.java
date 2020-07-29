package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.DoctorRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Doctor;
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

    @Query("SELECT d FROM Doctor d WHERE d.status!='D' AND d.id = :doctorId")
    Optional<Doctor> findDoctorById(@Param("doctorId") Long doctorId);

    @Query("SELECT d FROM Doctor d WHERE d.status='Y' AND d.id = :id")
    Optional<Doctor> findActiveDoctorById(@Param("id") Long id);

    @Query("SELECT d FROM Doctor d WHERE d.id = :doctorId AND d.status='Y' AND d.hospital.id=:hospitalId")
    Optional<Doctor> fetchDoctorById(@Param("doctorId") Long doctorId,@Param("hospitalId") Long hospitalId);
}
