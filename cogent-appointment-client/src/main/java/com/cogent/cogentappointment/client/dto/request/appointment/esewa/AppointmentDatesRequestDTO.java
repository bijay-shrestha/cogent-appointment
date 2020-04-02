package com.cogent.cogentappointment.client.dto.request.appointment.esewa;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDatesRequestDTO implements Serializable {

    private Long doctorId;

    private Long specializationId;
}
