package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 29/03/20
 */
@Repository
public interface AppointmentFollowUpRequestLogRepository extends JpaRepository<AppointmentFollowUpRequestLog, Long> {

    @Query("SELECT a FROM AppointmentFollowUpRequestLog a WHERE" +
            " a.appointmentFollowUpTracker.id =:appointmentFollowUpTrackerId ")
    Optional<AppointmentFollowUpRequestLog> fetchByFollowUpTrackerId(@Param("appointmentFollowUpTrackerId")
                                                                             Long appointmentFollowUpTrackerId);
}
