package com.cogent.cogentappointment.client.dto.request.appointment.esewa;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 17/04/20
 */
@Getter
@Setter
public class AvailableDoctorRequestDTO implements Serializable {

    private Date fromDate;

    private Date toDate;

    private Long doctorId;

    private Long specializationId;

    @NotNull
    private Long hospitalId;
}
