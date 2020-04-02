package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author smriti ON 12/02/2020
 */
@Repository
public interface AppointmentFollowUpTrackerRepository extends JpaRepository<AppointmentFollowUpTracker, Long>,
        AppointmentFollowUpTrackerRepositoryCustom {

    @Query("SELECT f FROM AppointmentFollowUpTracker f WHERE f.status = 'Y'")
    List<AppointmentFollowUpTracker> fetchActiveFollowUpTracker();

    @Query("SELECT f.id FROM AppointmentFollowUpTracker f WHERE f.status = 'Y' AND f.parentAppointmentId =:parentAppointmentId")
    Optional<Long> fetchByParentAppointmentId(@Param("parentAppointmentId") Long parentAppointmentId);
}
