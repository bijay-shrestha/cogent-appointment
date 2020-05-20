package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.client.repository.custom.HospitalDeptWeekDaysDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentWeekDaysDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 20/05/20
 */
@Repository
public interface HospitalDeptWeekDaysDutyRosterRepository extends JpaRepository<
        HospitalDepartmentWeekDaysDutyRoster, Long>, HospitalDeptWeekDaysDutyRosterRepositoryCustom {

}
