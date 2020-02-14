package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 2019-10-22
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, AppointmentRepositoryCustom {

    @Query(" SELECT a FROM Appointment a WHERE a.id=:id AND a.status = 'C'")
    Optional<Appointment> fetchRefundAppointmentById(@Param("id") Long id);

    @Query(" SELECT a FROM Appointment a WHERE a.id=:id AND a.status = 'PA'")
    Optional<Appointment> fetchPendingAppointmentById(@Param("id") Long id);


}
