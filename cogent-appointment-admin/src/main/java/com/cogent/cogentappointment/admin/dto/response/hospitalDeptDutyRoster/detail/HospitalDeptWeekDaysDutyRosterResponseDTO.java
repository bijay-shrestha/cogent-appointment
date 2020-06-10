package com.cogent.cogentappointment.admin.dto.response.hospitalDeptDutyRoster.detail;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 20/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptWeekDaysDutyRosterResponseDTO implements Serializable {

    private Long rosterWeekDaysId;

    private Date startTime;

    private Date endTime;

    private Character dayOffStatus;

    private Long weekDaysId;

    private String weekDaysName;

    private List<HospitalDeptWeekDaysDutyRosterDoctorInfoResponseDTO> weekDaysDoctorInfo;
}
