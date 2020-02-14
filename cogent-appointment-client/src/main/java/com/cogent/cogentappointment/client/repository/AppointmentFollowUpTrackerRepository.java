package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentFollowUpTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 12/02/2020
 */
@Repository
public interface AppointmentFollowUpTrackerRepository extends JpaRepository<AppointmentFollowUpTracker, Long>,
        AppointmentFollowUpTrackerRepositoryCustom {
}
