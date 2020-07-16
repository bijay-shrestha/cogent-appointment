package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.AppointmentHospitalDeptFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti ON 12/02/2020
 */
@Repository
public interface AppointmentHospitalDepartmentFollowUpTrackerRepository extends
        JpaRepository<AppointmentHospitalDepartmentFollowUpTracker, Long>, AppointmentHospitalDeptFollowUpTrackerRepositoryCustom {

    @Query("SELECT f.id FROM AppointmentHospitalDepartmentFollowUpTracker f WHERE f.status = 'Y' " +
            "AND f.parentAppointmentId =:parentAppointmentId")
    Optional<Long> fetchByParentAppointmentId(@Param("parentAppointmentId") Long parentAppointmentId);
}

