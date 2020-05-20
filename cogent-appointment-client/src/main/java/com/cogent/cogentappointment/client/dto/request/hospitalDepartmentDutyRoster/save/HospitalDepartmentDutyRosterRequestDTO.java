package com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.save;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class HospitalDepartmentDutyRosterRequestDTO implements Serializable {

    @NotNull
    private Long hospitalDepartmentId;

    @NotNull
    private Integer rosterGapDuration;

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @Status
    private Character hasOverrideDutyRoster;

    @NotNull
    @Status
    private Character isRoomEnabled;

    @NotEmpty
    private List<HospitalDeptWeekDaysDutyRosterRequestDTO> hospitalDeptWeekDaysDutyRosterRequestDTOS;

    private List<HospitalDeptDutyRosterOverrideRequestDTO> hospitalDeptDutyRosterOverrideRequestDTOS;
}
