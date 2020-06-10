package com.cogent.cogentappointment.esewa.dto.response.appointment.followup;

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

    private Character isFollowUp;

    private Double appointmentCharge;

    private Long parentAppointmentId;

    private Long appointmentReservationId;

    private Double refundPercentage;
}
