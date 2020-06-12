package com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 07/06/20
 */
@Getter
@Setter
public class HospitalDeptWeekDaysDutyRosterDoctorInfoUpdateRequestDTO implements Serializable {

    private Long hospitalDepartmentWeekDaysDutyRosterDoctorInfoId;

    private Long hospitalDepartmentDoctorInfoId;

    private Character status;
}
