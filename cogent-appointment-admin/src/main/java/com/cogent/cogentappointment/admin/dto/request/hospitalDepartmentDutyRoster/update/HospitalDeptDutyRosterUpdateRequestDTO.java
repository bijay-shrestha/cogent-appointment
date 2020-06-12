package com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update;

import lombok.*;

import javax.validation.constraints.NotEmpty;
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

    @NotEmpty
    private List<HospitalDeptWeekDaysDutyRosterUpdateRequestDTO> weekDaysDetail;
}
