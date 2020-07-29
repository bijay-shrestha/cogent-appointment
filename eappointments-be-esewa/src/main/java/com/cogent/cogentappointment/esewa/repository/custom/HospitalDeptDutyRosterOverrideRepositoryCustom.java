package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterAvailableDateResponseDTO;
import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 29/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterOverrideRepositoryCustom")
public interface HospitalDeptDutyRosterOverrideRepositoryCustom {

    HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRosterOverrideTimeInfo(Long hddRosterId,
                                                                                     Date date,
                                                                                     Long hospitalDepartmentId,
                                                                                     Long hospitalDepartmentRoomInfoId);

    List<HospitalDeptDutyRosterAvailableDateResponseDTO> fetchOverrideRosterDates(Long hddRosterId);

}
