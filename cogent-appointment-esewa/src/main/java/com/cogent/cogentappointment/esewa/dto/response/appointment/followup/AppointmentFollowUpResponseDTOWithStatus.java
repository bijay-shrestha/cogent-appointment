package com.cogent.cogentappointment.esewa.dto.response.appointment.followup;

import com.cogent.cogentappointment.esewa.dto.response.appointment.followup.AppointmentFollowUpResponseDTO;
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
