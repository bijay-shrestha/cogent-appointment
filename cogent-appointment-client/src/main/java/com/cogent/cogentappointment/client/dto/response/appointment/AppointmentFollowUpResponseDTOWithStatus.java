package com.cogent.cogentappointment.client.dto.response.appointment;

import com.cogent.cogentappointment.client.dto.response.appointment.esewa.AppointmentFollowUpResponseDTO;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author smriti on 16/02/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentFollowUpResponseDTOWithStatus implements Serializable {
    private AppointmentFollowUpResponseDTO responseDTO;

    private int responseCode;

    private HttpStatus responseStatus;
}
