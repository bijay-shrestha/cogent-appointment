package com.cogent.cogentappointment.esewa.dto.response.appointment.history;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 17/02/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseWithStatusDTO implements Serializable {

    private List<AppointmentResponseDTO> appointments;

    private int totalItems;

    private int responseCode;

    private HttpStatus responseStatus;
}
