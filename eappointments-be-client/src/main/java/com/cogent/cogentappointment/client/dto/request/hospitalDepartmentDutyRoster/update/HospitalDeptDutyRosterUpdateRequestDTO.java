package com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 27/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalDeptDutyRosterUpdateRequestDTO implements Serializable {

    private HospitalDeptDutyRosterUpdateDTO updateDetail;

    private HospitalDeptDutyRosterRoomUpdateRequestDTO roomDetail;

    private List<HospitalDeptWeekDaysDutyRosterUpdateRequestDTO> weekDaysDetail;
}
