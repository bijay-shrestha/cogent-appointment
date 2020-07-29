package com.cogent.cogentappointment.client.dto.response.doctorDutyRoster;

import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
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
public class DoctorDutyRosterDetailResponseDTO implements Serializable {

    private DoctorDutyRosterResponseDTO doctorDutyRosterInfo;

    private List<DoctorWeekDaysDutyRosterResponseDTO> weekDaysRosters;

    private List<DoctorDutyRosterOverrideResponseDTO> overrideRosters;
}

