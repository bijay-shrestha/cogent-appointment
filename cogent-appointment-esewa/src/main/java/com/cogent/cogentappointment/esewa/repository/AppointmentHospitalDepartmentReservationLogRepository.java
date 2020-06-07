package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.AppointmentHospitalDeptReservationLogRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentReservationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 02/06/20
 */
@Repository
public interface AppointmentHospitalDepartmentReservationLogRepository extends
        JpaRepository<AppointmentHospitalDepartmentReservationLog, Long>, AppointmentHospitalDeptReservationLogRepositoryCustom {

    @Query("SELECT a FROM AppointmentHospitalDepartmentReservationLog a WHERE a.id=:id")
    AppointmentHospitalDepartmentReservationLog findAppointmentReservationLogById(@Param("id") Long id);

    @Query("SELECT a FROM AppointmentHospitalDepartmentReservationLog a")
    List<AppointmentHospitalDepartmentReservationLog> fetchAppointmentHospitalDepartmentReservationLog();
}
