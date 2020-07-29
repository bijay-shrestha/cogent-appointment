package com.cogent.cogentappointment.esewa.dto.request.appointment.checkAvailibility;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 25/01/2020
 */
@Getter
@Setter
public class AppointmentCheckAvailabilityRequestDTO implements Serializable {

    @NotNull
    private Date appointmentDate;

    @NotNull
    private Long specializationId;

    @NotNull
    private Long doctorId;
}
