package com.cogent.cogentappointment.admin.dto.request.appointment.appointmentQueue;

import lombok.Getter;
import lombok.Setter;

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
}
