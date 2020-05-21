package com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.existing;

import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.detail.HospitalDeptDutyRosterOverrideResponseDTO;
import com.cogent.cogentappointment.client.dto.response.hospitalDeptDutyRoster.detail.HospitalDeptWeekDaysDutyRosterResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 01/02/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptExistingDutyRosterDetailResponseDTO implements Serializable {

    private List<HospitalDeptWeekDaysDutyRosterResponseDTO> weekDaysRosters;

    private List<HospitalDeptDutyRosterOverrideResponseDTO> overrideRosters;
}
