package com.cogent.cogentthirdpartyconnector.repository;

import com.cogent.cogentappointment.persistence.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 2019-10-22
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(" SELECT a FROM Appointment a WHERE a.status= 'PA' AND a.id=:id")
    Appointment fetchPendingAppointmentById(@Param("id") Long id);

}
