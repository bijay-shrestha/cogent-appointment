package com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Rupak
 */
@Getter
@Setter
public class AppointmentQueueRequestDTO implements Serializable {

    private Long hospitalId;

    private Long doctorId;

    private Date date;

    @NotNull
    @NotEmpty
    private String appointmentServiceType;

    private Long hospitalDepartmentId;
}
