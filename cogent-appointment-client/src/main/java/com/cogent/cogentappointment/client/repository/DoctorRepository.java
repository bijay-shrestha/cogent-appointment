package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.DoctorRepositoryCustom;
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

    @Query("SELECT d FROM Doctor d WHERE d.id = :doctorId AND d.hospital.id =:hospitalId AND d.status!='D'")
    Optional<Doctor> findDoctorByIdAndHospitalId(@Param("doctorId") Long doctorId,
                                                 @Param("hospitalId") Long hospitalId);

    @Query("SELECT d FROM Doctor d WHERE d.id = :doctorId AND d.hospital.id =:hospitalId AND d.status='Y'")
    Optional<Doctor> fetchActiveDoctorByIdAndHospitalId(@Param("doctorId") Long doctorId,
                                                        @Param("hospitalId") Long hospitalId);

    @Query("SELECT d FROM Doctor d WHERE d.id = :doctorId AND d.status='Y'")
    Optional<Doctor> fetchDoctorById(@Param("doctorId") Long doctorId);
}
