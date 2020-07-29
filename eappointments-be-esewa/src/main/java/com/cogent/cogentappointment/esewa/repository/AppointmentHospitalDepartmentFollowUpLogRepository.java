package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 13/02/20
 */
@Repository
public interface AppointmentHospitalDepartmentFollowUpLogRepository extends
        JpaRepository<AppointmentHospitalDepartmentFollowUpLog, Long> {

}
