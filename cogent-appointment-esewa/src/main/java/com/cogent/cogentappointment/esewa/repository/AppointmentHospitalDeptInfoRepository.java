package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.AppointmentHospitalDepartmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 05/06/20
 */
@Repository
public interface AppointmentHospitalDeptInfoRepository extends JpaRepository<AppointmentHospitalDepartmentInfo, Long> {
}
