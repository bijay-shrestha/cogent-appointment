package com.cogent.cogentappointment.client.dto.response.appointment.esewa.history;

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
public class AppointmentHistoryResponseWithStatusDTO implements Serializable {

    private List<AppointmentHistoryResponseDTO> appointments;

    private int responseCode;

    private HttpStatus responseStatus;
}
