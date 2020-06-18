package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.AppointmentHospitalDepartmentFollowUpTrackerRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 18/06/20
 */
@Repository
public interface AppointmentHospitalDepartmentFollowUpTrackerRepository extends
        JpaRepository<AppointmentHospitalDepartmentFollowUpTracker, Long>,
        AppointmentHospitalDepartmentFollowUpTrackerRepositoryCustom {
}
