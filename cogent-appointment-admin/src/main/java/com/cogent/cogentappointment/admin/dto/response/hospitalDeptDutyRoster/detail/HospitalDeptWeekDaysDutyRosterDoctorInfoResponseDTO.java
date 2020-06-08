package com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.detail;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 07/06/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptWeekDaysDutyRosterDoctorInfoResponseDTO implements Serializable {

    private Long hospitalDepartmentWeekDaysDutyRosterDoctorInfoId;

    private Long hospitalDepartmentDoctorInfoId;

    private String doctorName;

    private String fileUri;
}
