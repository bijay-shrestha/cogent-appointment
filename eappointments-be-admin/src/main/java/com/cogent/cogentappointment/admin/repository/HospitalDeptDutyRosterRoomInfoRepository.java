package com.cogent.cogentappointment.admin.repository;

import com.cogent.cogentappointment.admin.repository.custom.HospitalDeptDutyRosterRoomInfoRepositoryCustom;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author smriti on 20/05/20
 */
@Repository
public interface HospitalDeptDutyRosterRoomInfoRepository extends JpaRepository<HospitalDepartmentDutyRosterRoomInfo, Long>,
        HospitalDeptDutyRosterRoomInfoRepositoryCustom {

    @Query("SELECT d FROM HospitalDepartmentDutyRosterRoomInfo d WHERE d.status = 'Y' AND d.id =:id")
    Optional<HospitalDepartmentDutyRosterRoomInfo> fetchById(@Param("id") Long id);
}
