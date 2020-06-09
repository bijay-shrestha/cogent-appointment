package com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus;

import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
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
public class HospitalDeptAndDoctorDTO implements Serializable {

    private Long hospitalDepartmentId;

    private List<DoctorDropdownDTO> doctorInfo;

}
