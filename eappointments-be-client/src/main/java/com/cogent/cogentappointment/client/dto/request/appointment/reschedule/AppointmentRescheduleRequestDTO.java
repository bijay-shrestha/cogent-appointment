package com.cogent.cogentappointment.client.dto.request.appointment.reschedule;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * @author smriti ON 08/12/2019
 */
@Getter
@Setter
public class AppointmentRescheduleRequestDTO implements Serializable {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Date rescheduleDate;

    @NotNull
    @NotBlank
    private String rescheduleTime;

    @NotNull
    @NotEmpty
    @NotBlank
    private String remarks;
}
