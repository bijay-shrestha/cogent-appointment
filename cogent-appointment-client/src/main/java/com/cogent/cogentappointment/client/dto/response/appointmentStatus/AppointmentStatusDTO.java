package com.cogent.cogentappointment.client.dto.response.appointmentStatus;

import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.client.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 16/02/20
 */
@Getter
@Setter
@Builder
public class AppointmentStatusDTO implements Serializable {

    private List<DoctorDutyRosterStatusResponseDTO> doctorDutyRosterInfo;

    private List<DoctorDropdownDTO> doctorInfo;

    private AppointmentStatusCountResponseDTO appointmentStatusCount;

}
