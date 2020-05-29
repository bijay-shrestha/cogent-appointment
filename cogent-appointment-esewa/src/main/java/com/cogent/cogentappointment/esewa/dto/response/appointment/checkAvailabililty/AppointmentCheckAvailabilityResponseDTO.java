package com.cogent.cogentappointment.esewa.dto.response.appointment.checkAvailabililty;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 25/01/2020
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCheckAvailabilityResponseDTO implements Serializable {

    private Long roomId;

    private String roomNumber;

    private String hospitalDepartmentAvailableTime;

    private List<String> availableTimeSlots;
}
