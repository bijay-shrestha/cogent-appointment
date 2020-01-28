package com.cogent.cogentappointment.admin.dto.response.appointment;

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

    private String doctorStartTime;

    private String doctorEndTime;

    private Character dayOffStatus;

    private List<AppointmentAvailabilityResponseDTO> availableAppointments;
}
