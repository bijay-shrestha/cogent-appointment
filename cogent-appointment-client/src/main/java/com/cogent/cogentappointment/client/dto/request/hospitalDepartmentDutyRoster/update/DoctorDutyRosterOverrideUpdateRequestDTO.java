package com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update;

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
    private Long hddRosterId;

    private Long rosterOverrideId;

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

