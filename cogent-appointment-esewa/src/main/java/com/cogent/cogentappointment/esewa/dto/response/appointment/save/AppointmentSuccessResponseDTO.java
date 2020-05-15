package com.cogent.cogentappointment.esewa.dto.response.appointment.save;

import lombok.*;
import org.springframework.http.HttpStatus;

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

    private int responseCode;

    private HttpStatus responseStatus;
}
