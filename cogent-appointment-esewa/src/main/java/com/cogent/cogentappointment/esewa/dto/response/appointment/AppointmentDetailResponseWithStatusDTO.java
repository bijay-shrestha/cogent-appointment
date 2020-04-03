package com.cogent.cogentappointment.esewa.dto.response.appointment;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti on 17/02/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDetailResponseWithStatusDTO implements Serializable {

    private AppointmentDetailResponseDTO appointmentDetailResponseDTO;

    private int responseCode;

    private HttpStatus responseStatus;
}
