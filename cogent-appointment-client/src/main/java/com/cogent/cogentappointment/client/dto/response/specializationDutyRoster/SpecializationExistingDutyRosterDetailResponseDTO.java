package com.cogent.cogentappointment.client.dto.response.specializationDutyRoster;

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
public class SpecializationExistingDutyRosterDetailResponseDTO implements Serializable {

    private List<SpecializationWeekDaysDutyRosterResponseDTO> weekDaysRosters;

    private List<SpecializationDutyRosterOverrideResponseDTO> overrideRosters;
}
