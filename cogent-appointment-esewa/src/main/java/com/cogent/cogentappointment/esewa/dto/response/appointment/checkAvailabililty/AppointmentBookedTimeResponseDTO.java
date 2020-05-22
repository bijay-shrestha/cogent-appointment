package com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 09/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentBookedTimeResponseDTO implements Serializable {

    private String appointmentTime;

}
