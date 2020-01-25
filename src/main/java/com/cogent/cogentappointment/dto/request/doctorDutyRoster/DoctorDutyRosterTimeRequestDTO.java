package com.cogent.cogentappointment.dto.request.doctorDutyRoster;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 10/12/2019
 */
@Getter
@Setter
public class DoctorDutyRosterTimeRequestDTO implements Serializable {

    @NotNull
    private Date date;

    @NotNull
    private Long doctorId;

    @NotNull
    private Long specializationId;
}


