package com.cogent.cogentappointment.esewa.dto.request.appointment.eSewa;

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
