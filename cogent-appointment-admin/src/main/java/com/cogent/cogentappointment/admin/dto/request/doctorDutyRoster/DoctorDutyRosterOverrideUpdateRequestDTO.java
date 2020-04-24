package com.cogent.cogentappointment.admin.dto.request.doctorDutyRoster;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDutyRosterOverrideUpdateRequestDTO implements Serializable {

    @NotNull
    private Long doctorDutyRosterId;

    private Long doctorDutyRosterOverrideId;

    @NotNull
    private Date overrideFromDate;

    @NotNull
    private Date overrideToDate;

    private Date startTime;

    private Date endTime;

    @NotNull
    private Character dayOffStatus;

    @NotNull
    private Character status;

    @NotEmpty
    @NotBlank
    @NotNull
    private String remarks;
}

