package com.cogent.cogentappointment.client.dto.response.appointment.appoinmentDateAndTime;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDatesResponseDTO implements Serializable {

    private Long doctorId;

    private Long specializationId;

    private List<AvailableDatesResponseDTO> dates;

    private int responseCode;

    private HttpStatus responseStatus;
}
