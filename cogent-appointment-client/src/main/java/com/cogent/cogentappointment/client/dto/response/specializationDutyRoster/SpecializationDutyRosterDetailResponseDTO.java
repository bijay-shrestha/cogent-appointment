package com.cogent.cogentappointment.client.dto.response.specializationDutyRoster;

import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterOverrideResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 29/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationDutyRosterDetailResponseDTO implements Serializable {

    private SpecializationDutyRosterResponseDTO specializationDutyRosterInfo;

    private List<SpecializationWeekDaysDutyRosterResponseDTO> weekDaysRosters;

    private List<DoctorDutyRosterOverrideResponseDTO> overrideRosters;
}

