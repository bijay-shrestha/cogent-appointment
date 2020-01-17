package com.cogent.cogentappointment.dto.response.appointment;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti ON 09/12/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentAvailabilityResponseDTO implements Serializable {

    private List<AppointmentTimeResponseDTO> appointmentTimeResponseDTOS;

//    private DoctorDutyRosterTimeResponseDTO doctorDutyRosterTimeResponseDTO;
}
