package com.cogent.cogentappointment.admin.dto.response.appointment.appointmentStatus.departmentAppointmentStatus;

import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sauravi Thapa ON 6/9/20
 */
@Getter
@Setter
public class HospitalDepartmentDoctorListDTO implements Serializable {

    private Long hospitalDepartmentId;

    private List<DoctorDropdownDTO> doctorInfo;
}
