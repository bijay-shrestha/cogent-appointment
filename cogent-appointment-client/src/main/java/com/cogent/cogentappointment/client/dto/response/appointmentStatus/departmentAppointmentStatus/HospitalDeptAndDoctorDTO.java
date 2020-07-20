package com.cogent.cogentappointment.client.dto.response.appointmentStatus.departmentAppointmentStatus;

import com.cogent.cogentappointment.client.dto.response.doctor.DoctorDropdownDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa ON 6/5/20
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalDeptAndDoctorDTO implements Serializable {

    private Long hospitalDepartmentId;

    private List<DoctorDropdownDTO> doctorInfo;

}
