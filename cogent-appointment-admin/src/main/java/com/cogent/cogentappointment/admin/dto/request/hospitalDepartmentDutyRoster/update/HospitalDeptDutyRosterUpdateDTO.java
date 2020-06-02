package com.cogent.cogentappointment.admin.dto.request.hospitalDepartmentDutyRoster.update;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 21/05/20
 */
@Getter
@Setter
public class HospitalDeptDutyRosterUpdateDTO implements Serializable {

    @NotNull
    private Long hddRosterId;

    @NotNull
    private Integer rosterGapDuration;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @Status
    private Character hasOverrideDutyRoster;

    private Character isRoomEnabled;

    @NotEmpty
    @NotBlank
    @NotNull
    private String remarks;

    @Status
    private Character isRoomUpdated;


}
