package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.esewa.repository.custom.HospitalDeptDutyRosterRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * @author smriti on 29/05/20
 */
public interface HospitalDeptDutyRosterRepository extends JpaRepository<HospitalDepartmentDutyRoster, Long>,
        HospitalDeptDutyRosterRepositoryCustom {

    @Query("SELECT h FROM HospitalDepartmentDutyRoster h WHERE h.status = 'Y' AND h.id =:id")
    Optional<HospitalDepartmentDutyRoster> fetchById(@Param("id") Long id);
}
