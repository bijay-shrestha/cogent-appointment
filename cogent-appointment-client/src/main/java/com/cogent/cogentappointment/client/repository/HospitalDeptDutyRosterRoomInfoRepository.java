package com.cogent.cogentappointment.client.repository;

import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRosterRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 20/05/20
 */
@Repository
public interface HospitalDeptDutyRosterRoomInfoRepository extends JpaRepository<HospitalDepartmentDutyRosterRoomInfo, Long> {
}
