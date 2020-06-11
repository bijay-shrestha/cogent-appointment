package com.cogent.cogentappointment.esewa.repository.custom;

import com.cogent.cogentappointment.esewa.dto.response.hospitalDepartmentDutyRoster.HospitalDeptDutyRosterTimeResponseTO;
import com.cogent.cogentappointment.persistence.model.HospitalDepartmentDutyRoster;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author smriti on 29/05/20
 */
@Repository
@Qualifier("hospitalDeptDutyRosterRepositoryCustom")
public interface HospitalDeptDutyRosterRepositoryCustom {

    List<HospitalDepartmentDutyRoster> fetchHospitalDeptDutyRoster(Date date,
                                                                   Long hospitalDepartmentId);

    HospitalDepartmentDutyRoster fetchHospitalDeptDutyRosterWithoutRoom(Date date, Long hospitalDepartmentId);

    HospitalDepartmentDutyRoster fetchHospitalDeptDutyRosterWithRoom(Date date, Long hospitalDepartmentId,
                                                                     Long hospitalDepartmentRoomInfoId);

    HospitalDeptDutyRosterTimeResponseTO fetchHospitalDeptDutyRoster(Date appointmentDate,
                                                                     Long hospitalDepartmentId,
                                                                     Long hospitalDepartmentRoomInfoId);

    List<HospitalDepartmentDutyRoster> fetchHospitalDeptDutyRoster(Long hospitalDepartmentId);


}
