package com.cogent.cogentappointment.client.dto.request.doctorDutyRoster;

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
public class DoctorExistingDutyRosterRequestDTO implements Serializable {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long specializationId;

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;
}
