package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDeptDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author smriti on 29/05/20
 */
public interface HospitalDeptDutyRosterRepository extends JpaRepository<HospitalDepartmentDutyRoster, Long>,
        HospitalDeptDutyRosterRepositoryCustom {
}
