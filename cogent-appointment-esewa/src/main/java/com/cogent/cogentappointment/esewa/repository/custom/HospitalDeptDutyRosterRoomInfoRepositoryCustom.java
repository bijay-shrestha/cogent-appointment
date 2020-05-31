package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author smriti on 31/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterRoomInfoRepositoryCustom")
public interface HospitalDeptDutyRosterRoomInfoRepositoryCustom {

    HospitalDeptDutyRosterRoomInfoResponseDTO fetchHospitalDeptRoomInfo(Long hddRosterId);
}
