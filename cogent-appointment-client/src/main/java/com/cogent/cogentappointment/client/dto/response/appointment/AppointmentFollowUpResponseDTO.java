package com.cogent.cogentappointment.client.dto.response.appointment;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 16/02/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentFollowUpResponseDTO implements Serializable {

    private Character isFreeFollowUp;

    private Double appointmentCharge;

    private Long parentAppointmentId;

    private Long appointmentReservationId;
}
