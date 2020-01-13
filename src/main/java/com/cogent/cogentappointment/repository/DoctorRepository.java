package com.cogent.cogentappointment.repository;

import com.cogent.cogentappointment.model.Doctor;
import com.cogent.cogentappointment.repository.custom.DoctorRepositoryCustom;
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

    @Query("SELECT c FROM Doctor c WHERE c.status!='D' AND c.id = :id")
    Optional<Doctor> findDoctorById(@Param("id") Long id);

    @Query("SELECT c FROM Doctor c WHERE c.status='Y' AND c.id = :id")
    Optional<Doctor> findActiveDoctorById(@Param("id") Long id);
}
