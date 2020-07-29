package com.cogent.cogentappointment.client.dto.request.hospitalDepartmentDutyRoster;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 01/02/2020
 */
@Getter
@Setter
public class HospitalDeptExistingDutyRosterRequestDTO implements Serializable {

    @NotNull
    private Long hospitalDeptId;

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

}
