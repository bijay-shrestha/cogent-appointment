package com.cogent.cogentappointment.admin.dto.request.appointment.appointmentStatus;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 16/12/2019
 */
@Getter
@Setter
public class AppointmentStatusRequestDTO implements Serializable {

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    private Long hospitalId;

    private Long doctorId;

    private Long specializationId;

    /*V= VACANT
     * PA= PENDING APPROVAL
     * A= APPROVAL
     * C= CANCELLED
     * ALL = EMPTY
     * */
    private String status;

    private String appointmentNumber;

    private Character hasAppointmentNumber;
}
