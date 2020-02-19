package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 18/02/20
 */
@Repository
public interface AppointmentReservationRepository extends JpaRepository<AppointmentReservationLog, Long> {
}
