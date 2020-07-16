package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Sauravi Thapa ON 6/5/20
 */
@Getter
@Setter
@Builder
public class HospitalDeptAppointmentStatusDTO implements Serializable {

    private List<HospitalDeptDutyRosterStatusResponseDTO> hospitalDeptDutyRosterInfo;

    private List<HospitalDeptAndDoctorDTO> hospitalDeptAndDoctorInfo;

}
