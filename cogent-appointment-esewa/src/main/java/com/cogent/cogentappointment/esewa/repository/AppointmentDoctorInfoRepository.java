package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentDoctorInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author smriti on 05/06/20
 */
public interface AppointmentDoctorInfoRepository extends JpaRepository<AppointmentDoctorInfo, Long> {
}
