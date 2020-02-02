package com.cogent.cogentappointment.client.dto.response.doctorDutyRoster;

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
public class DoctorExistingDutyRosterDetailResponseDTO implements Serializable {

    private List<DoctorWeekDaysDutyRosterResponseDTO> weekDaysRosters;

    private List<DoctorDutyRosterOverrideResponseDTO> overrideRosters;
}
