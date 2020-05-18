package com.cogent.cogentappointment.client.dto.request.specializationDutyRoster;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author smriti on 26/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationDutyRosterRequestDTO implements Serializable {

    @NotNull
    private Long specializationId;

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

    @NotEmpty
    private List<SpecializationWeekDaysDutyRosterRequestDTO> specializationWeekDaysDutyRosterRequestDTOS;

    private List<SpecializationDutyRosterOverrideRequestDTO> specializationDutyRosterOverrideRequestDTOS;
}
