package com.cogent.cogentappointment.client.dto.response.appointment;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentMinResponseWithStatusDTO {
    List<AppointmentMinResponseDTO> appointmentMinResponseDTOS;

    private int status;
}
