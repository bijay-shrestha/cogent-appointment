package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRosterDoctorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 05/06/20
 */
@Repository
public interface HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository extends
        JpaRepository<HospitalDepartmentWeekDaysDutyRosterDoctorInfo, Long>, HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepositoryCustom {


}
