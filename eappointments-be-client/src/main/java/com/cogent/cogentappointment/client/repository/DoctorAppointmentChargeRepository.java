package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.DoctorAppointmentCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 13/01/2020
 */
@Repository
public interface DoctorAppointmentChargeRepository extends JpaRepository<DoctorAppointmentCharge, Long> {

    @Query("SELECT d FROM DoctorAppointmentCharge d WHERE d.doctorId.id=:doctorId AND d.doctorId.status !='D'")
    Optional<DoctorAppointmentCharge> findByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT d.id FROM DoctorAppointmentCharge d WHERE d.doctorId.id=:doctorId AND d.appointmentCharge=:charge")
    Optional<Long> findByDoctorIdAndCharge(@Param("doctorId") Long doctorId,
                                           @Param("charge") Double charge);
}
