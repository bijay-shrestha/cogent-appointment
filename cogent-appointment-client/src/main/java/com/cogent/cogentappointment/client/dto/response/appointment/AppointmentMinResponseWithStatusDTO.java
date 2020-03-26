package com.cogent.cogentappointment.client.dto.response.appointment;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentMinResponseWithStatusDTO {
    List<AppointmentMinResponseDTO> appointmentMinResponseDTOS;

    private int responseCode;

    private HttpStatus responseStatus;
}
