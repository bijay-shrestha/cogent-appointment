package com.cogent.cogentappointment.client.dto.request.doctorDutyRoster;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class DoctorDutyRosterUpdateRequestDTO implements Serializable {

    @NotNull
    private Long doctorDutyRosterId;

    @NotNull
    private Integer rosterGapDuration;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @Status
    private Character hasOverrideDutyRoster;

    @NotEmpty
    @NotBlank
    @NotNull
    private String remarks;

    @NotEmpty
    private List<DoctorWeekDaysDutyRosterUpdateRequestDTO> weekDaysDutyRosterUpdateRequestDTOS;
}
