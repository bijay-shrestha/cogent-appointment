package com.cogent.cogentappointment.esewa.dto.response.appointment.cancel;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti on 22/05/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCancelResponseDTO implements Serializable {

    private Double appointmentAmount;

    private Double refundAmount;

    private String message;

    private int responseCode;

    private HttpStatus responseStatus;
}
