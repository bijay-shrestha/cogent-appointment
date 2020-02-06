package com.cogent.cogentappointment.client.dto.response.appointment;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti ON 05/02/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSuccessResponseDTO implements Serializable {

    private String appointmentNumber;

    private Character appointmentTransactionStatus;
}
