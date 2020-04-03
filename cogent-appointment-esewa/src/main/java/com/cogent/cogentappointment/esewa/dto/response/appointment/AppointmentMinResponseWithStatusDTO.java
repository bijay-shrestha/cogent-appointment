package com.cogent.cogentappointment.esewa.dto.response.appointment;

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
