package com.cogent.cogentappointment.esewa.repository;


import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti ON 04/02/2020
 */
@Repository
public interface AppointmentTransactionDetailRepository extends JpaRepository<AppointmentTransactionDetail, Long> {

    @Query("SELECT a.appointmentAmount FROM AppointmentTransactionDetail a WHERE a.appointment.id = :appointmentId")
    Double fetchAppointmentAmount(@Param("appointmentId") Long appointmentId);
}
