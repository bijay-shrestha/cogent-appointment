package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus;

import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import com.cogent.cogentappointment.admin.dto.response.doctorDutyRoster.DoctorDutyRosterStatusResponseDTO;
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

}
