package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentRescheduleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 12/02/2020
 */
@Repository
public interface AppointmentRescheduleLogRepository extends JpaRepository<AppointmentRescheduleLog, Long> {
}
