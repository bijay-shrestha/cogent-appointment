package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 26/05/20
 */
@Repository
public interface HospitalAppointmentServiceTypeRepository extends JpaRepository<HospitalAppointmentServiceType, Long> {
}
