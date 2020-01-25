package com.cogent.cogentappointment.dto.response.appointment;

import com.cogent.cogentappointment.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
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
    private List<AppointmentAvailabilityResponseDTO> availableAppointments;

    private DoctorDutyRosterTimeResponseDTO doctorDutyRosterTimeInfo;
}
