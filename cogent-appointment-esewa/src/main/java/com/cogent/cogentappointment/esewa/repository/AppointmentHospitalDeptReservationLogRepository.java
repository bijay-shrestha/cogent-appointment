package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.AppointmentHospitalDeptReservationLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 02/06/20
 */
@Repository
public interface AppointmentHospitalDeptReservationLogRepository extends
        JpaRepository<AppointmentHospitalDepartmentReservationLog, Long>, AppointmentHospitalDeptReservationLogRepositoryCustom {
//
//    @Query("SELECT a FROM AppointmentReservationLog a WHERE a.id=:id")
//    AppointmentReservationLog findAppointmentReservationLogById(@Param("id") Long id);
//
//    @Query("SELECT a FROM AppointmentReservationLog a")
//    List<AppointmentReservationLog> fetchAppointmentReservationLog();
}
