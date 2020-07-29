package com.cogent.cogentappointment.client.dto.response.appointment;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 18/12/2019
 */
@Getter
@Setter
public class AppointmentBookedDateResponseDTO implements Serializable {

    private Date appointmentDate;
}
