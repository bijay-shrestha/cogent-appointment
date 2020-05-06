package com.cogent.cogentappointment.scheduler.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 18/02/20
 */
@Repository
public interface AppointmentReservationRepository extends JpaRepository<AppointmentReservationLog, Long> {

    @Query("SELECT a FROM AppointmentReservationLog a")
    List<AppointmentReservationLog> fetchAppointmentReservationLog();
}
