package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentReservationLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 18/02/20
 */
@Repository
public interface AppointmentReservationLogRepository extends JpaRepository<AppointmentReservationLog, Long>,
        AppointmentReservationLogRepositoryCustom {
}
