package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus;

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
public class DeptAppointmentStatusDTO implements Serializable {

    private List<DeptDutyRosterStatusResponseDTO> doctorDutyRosterInfo;

    private List<DoctorDropdownDTO> doctorInfo;

}
