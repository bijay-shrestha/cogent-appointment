package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterRoomInfoResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author smriti on 31/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterRoomInfoRepositoryCustom")
public interface HospitalDeptDutyRosterRoomInfoRepositoryCustom {

    List<HospitalDeptDutyRosterRoomInfoResponseDTO> fetchHospitalDeptRoomInfo(List<Long> hddRosterId);

    String fetchRoomNumber(Long hddRosterId, Long roomId);
}
