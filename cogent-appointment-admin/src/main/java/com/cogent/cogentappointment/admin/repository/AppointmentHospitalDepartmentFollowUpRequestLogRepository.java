package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentFollowUpRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 29/03/20
 */
@Repository
public interface AppointmentHospitalDepartmentFollowUpRequestLogRepository extends
        JpaRepository<AppointmentHospitalDepartmentFollowUpRequestLog, Long> {

}
