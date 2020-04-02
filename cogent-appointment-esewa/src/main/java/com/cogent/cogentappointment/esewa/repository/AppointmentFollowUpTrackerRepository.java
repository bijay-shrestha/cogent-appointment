package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.AppointmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti ON 12/02/2020
 */
@Repository
public interface AppointmentFollowUpTrackerRepository extends JpaRepository<AppointmentFollowUpTracker, Long>,
        AppointmentFollowUpTrackerRepositoryCustom {

    @Query("SELECT f FROM AppointmentFollowUpTracker f WHERE f.status = 'Y'")
    List<AppointmentFollowUpTracker> fetchActiveFollowUpTracker();
}
