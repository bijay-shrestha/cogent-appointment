package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDeptDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 29/05/20
 */
@Repository
public interface HospitalDeptDutyRosterOverrideRepository extends JpaRepository<HospitalDepartmentDutyRosterOverride, Long>,
        HospitalDeptDutyRosterRepositoryCustom {
}
