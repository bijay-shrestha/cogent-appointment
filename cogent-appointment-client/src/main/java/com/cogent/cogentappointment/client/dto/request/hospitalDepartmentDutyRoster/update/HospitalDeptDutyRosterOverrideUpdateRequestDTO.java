package com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster.update;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class HospitalDeptDutyRosterOverrideUpdateRequestDTO implements Serializable {

    @NotNull
    private Long hddRosterId;

    private Long rosterOverrideId;

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    private Date startTime;

    private Date endTime;

    @NotNull
    private Character dayOffStatus;

    @NotNull
    private Character status;

    private Long hospitalDepartmentRoomInfoId;

    @NotEmpty
    @NotBlank
    @NotNull
    private String remarks;
}

