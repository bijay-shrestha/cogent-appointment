package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentTransactionDetailRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 04/02/2020
 */
@Repository
public interface AppointmentTransactionDetailRepository extends JpaRepository<AppointmentTransactionDetail, Long>,
        AppointmentTransactionDetailRepositoryCustom {

    @Query(" SELECT atd FROM AppointmentTransactionDetail atd WHERE atd.appointment.id=:appointmentId")
    Optional<AppointmentTransactionDetail> fetchByAppointmentId(@Param("appointmentId") Long appointmentId);
}
