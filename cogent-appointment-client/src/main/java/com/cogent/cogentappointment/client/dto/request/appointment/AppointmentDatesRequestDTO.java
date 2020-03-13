package com.cogent.cogentappointment.client.dto.request.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDatesRequestDTO implements Serializable {

    private Long DoctorId;

    private Long specializationId;
}
