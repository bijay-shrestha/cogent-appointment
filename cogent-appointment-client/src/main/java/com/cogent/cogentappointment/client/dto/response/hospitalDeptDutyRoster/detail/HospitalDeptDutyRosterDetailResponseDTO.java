package com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.detail;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 20/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptDutyRosterDetailResponseDTO implements Serializable {

    private HospitalDeptDutyRosterResponseDTO dutyRosterDetail;

    private HospitalDeptDutyRosterRoomResponseDTO roomInfo;

    private List<HospitalDeptWeekDaysDutyRosterResponseDTO> weekDaysRosters;

    private List<HospitalDeptDutyRosterOverrideResponseDTO> overrideRosters;
}

