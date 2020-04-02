package com.cogent.cogentappointment.client.dto.request.appointment.appointmentQueue;

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
    private Long doctorId;

    private Date date;
}
