package com.cogent.cogentappointment.esewa.dto.request.appointment.approval;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 12/02/2020
 */
@Getter
@Setter
public class AppointmentRejectDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    @NotEmpty
    private String remarks;
}
