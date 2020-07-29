package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 05/06/20
 */
@Repository
public interface AppointmentHospitalDepartmentInfoRepository extends JpaRepository<AppointmentHospitalDepartmentInfo, Long> {

    @Query(" SELECT a FROM AppointmentHospitalDepartmentInfo a WHERE a.appointment.id =:appointmentId")
    Optional<AppointmentHospitalDepartmentInfo> fetchAppointmentHospitalDepartmentInfo(
            @Param("appointmentId") Long appointmentId);
}
