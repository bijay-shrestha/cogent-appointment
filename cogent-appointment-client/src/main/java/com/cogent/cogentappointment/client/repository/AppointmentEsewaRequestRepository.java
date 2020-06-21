package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentEsewaRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Sauravi Thapa ON 6/19/20
 */

@Repository
public interface AppointmentEsewaRequestRepository extends JpaRepository<AppointmentEsewaRequest,Long> {

    @Query("SELECT a.esewaId FROM AppointmentEsewaRequest a WHERE a.appointment.id=:appointmentId")
    String fetchEsewaIdByAppointmentId(@Param("appointmentId") Long appointmentId);

}
