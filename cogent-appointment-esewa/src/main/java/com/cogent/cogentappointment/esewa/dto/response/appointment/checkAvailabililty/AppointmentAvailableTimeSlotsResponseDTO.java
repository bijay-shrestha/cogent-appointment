package com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 23/06/20
 */
@Getter
@Setter
public class AppointmentAvailableTimeSlotsResponseDTO implements Serializable {

    private String appointmentTime;

    private String appointmentTimeNepali;
}
