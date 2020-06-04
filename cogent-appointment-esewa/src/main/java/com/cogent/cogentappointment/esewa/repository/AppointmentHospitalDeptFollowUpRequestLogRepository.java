package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 29/03/20
 */
@Repository
public interface AppointmentHospitalDeptFollowUpRequestLogRepository
        extends JpaRepository<AppointmentHospitalDepartmentFollowUpRequestLog, Long> {

    @Query("SELECT a.followUpRequestedCount FROM AppointmentHospitalDepartmentFollowUpRequestLog a WHERE" +
            " a.appointmentHospitalDepartmentFollowUpTracker.id =:appointmentFollowUpTrackerId ")
    Optional<Integer> fetchRequestCountByFollowUpTrackerId(@Param("appointmentFollowUpTrackerId")
                                                                   Long appointmentFollowUpTrackerId);

    @Query("SELECT a FROM AppointmentHospitalDepartmentFollowUpRequestLog a WHERE" +
            " a.appointmentHospitalDepartmentFollowUpTracker.id =:appointmentFollowUpTrackerId ")
    Optional<AppointmentHospitalDepartmentFollowUpRequestLog> fetchByFollowUpTrackerId(
            @Param("appointmentFollowUpTrackerId") Long appointmentFollowUpTrackerId);
}
