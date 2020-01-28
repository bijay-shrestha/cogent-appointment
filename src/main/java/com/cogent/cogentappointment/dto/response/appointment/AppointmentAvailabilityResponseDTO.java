package com.cogent.cogentappointment.dto.response.appointment;

import com.cogent.cogentappointment.dto.response.doctorDutyRoster.DoctorDutyRosterTimeResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
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

    private String startTime;

    private String endTime;
}
