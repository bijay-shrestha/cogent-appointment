package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 13/02/20
 */
@Repository
public interface AppointmentHospitalDepartmentFollowUpLogRepository extends
        JpaRepository<AppointmentHospitalDepartmentFollowUpLog, Long> {

    @Query("SELECT a FROM AppointmentHospitalDepartmentFollowUpLog a WHERE a.followUpAppointmentId =:id")
    Optional<AppointmentHospitalDepartmentFollowUpLog> findByFollowUpAppointmentId(@Param("id") Long id);
}
