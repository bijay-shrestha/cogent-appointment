package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus;

import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa ON 6/5/20
 */
@Getter
@Setter
@Builder
public class HospitalDeptAppointmentStatusDTO implements Serializable {

    private List<HospitalDeptDutyRosterStatusResponseDTO> doctorDutyRosterInfo;

}
