package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentDoctorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 05/06/20
 */
@Repository
public interface AppointmentDoctorInfoRepository extends JpaRepository<AppointmentDoctorInfo, Long> {

    @Query(" SELECT a FROM AppointmentDoctorInfo a WHERE a.appointment.id =:appointmentId")
    Optional<AppointmentDoctorInfo> fetchAppointmentDoctorInfo(@Param("appointmentId") Long appointmentId);
}
