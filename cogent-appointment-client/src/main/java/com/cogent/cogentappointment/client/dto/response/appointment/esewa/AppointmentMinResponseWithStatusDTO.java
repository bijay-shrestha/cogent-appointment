package com.cogent.cogentappointment.client.dto.response.appointment.esewa;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentMinResponseWithStatusDTO {
    private List<AppointmentMinResponseDTO> appointmentMinResponseDTOS;

    private int responseCode;

    private HttpStatus responseStatus;
}
