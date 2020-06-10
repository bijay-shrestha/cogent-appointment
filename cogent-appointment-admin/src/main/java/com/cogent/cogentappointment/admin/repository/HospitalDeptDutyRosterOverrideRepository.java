package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.HospitalDeptDutyRosterOverrideRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 20/05/20
 */
@Repository
public interface HospitalDeptDutyRosterOverrideRepository extends JpaRepository<HospitalDepartmentDutyRosterOverride, Long>,
        HospitalDeptDutyRosterOverrideRepositoryCustom {

    @Query("SELECT d FROM HospitalDepartmentDutyRosterOverride d WHERE d.status = 'Y' AND d.id =:id")
    Optional<HospitalDepartmentDutyRosterOverride> fetchById(@Param("id") Long id);

}
