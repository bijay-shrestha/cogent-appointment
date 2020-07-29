package com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.save;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
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
    private Long hospitalId;

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

    private Long hospitalDepartmentRoomInfoId;

    @NotEmpty
    private List<HospitalDeptWeekDaysDutyRosterRequestDTO> weekDaysDetail;

    private List<HospitalDeptDutyRosterOverrideRequestDTO> overrideDetail;
}
