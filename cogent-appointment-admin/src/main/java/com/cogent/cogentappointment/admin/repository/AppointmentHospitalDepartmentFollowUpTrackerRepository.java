package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.AppointmentHospitalDepartmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 18/06/20
 */
@Repository
public interface AppointmentHospitalDepartmentFollowUpTrackerRepository extends
        JpaRepository<AppointmentHospitalDepartmentFollowUpTracker, Long>,
        AppointmentHospitalDepartmentFollowUpTrackerRepositoryCustom {

    @Query("SELECT f FROM AppointmentHospitalDepartmentFollowUpTracker f WHERE f.status = 'Y'")
    List<AppointmentHospitalDepartmentFollowUpTracker> fetchActiveFollowUpTracker();
}
