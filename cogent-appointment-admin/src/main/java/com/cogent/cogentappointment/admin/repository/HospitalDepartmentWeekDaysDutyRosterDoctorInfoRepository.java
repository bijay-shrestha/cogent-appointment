package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRosterDoctorInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 05/06/20
 */
@Repository
public interface HospitalDepartmentWeekDaysDutyRosterDoctorInfoRepository extends
        JpaRepository<HospitalDepartmentWeekDaysDutyRosterDoctorInfo, Long> {

    @Query("SELECT h FROM HospitalDepartmentWeekDaysDutyRosterDoctorInfo h WHERE h.status = 'Y' AND h.id =:id")
    Optional<HospitalDepartmentWeekDaysDutyRosterDoctorInfo> fetchById(@Param("id") Long id);
}
