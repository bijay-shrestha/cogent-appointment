package com.cogent.cogentappointment.admin.dto.response.hospitalDepartment;

import com.cogent.cogentappointment.admin.dto.commons.DropDownResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import com.cogent.cogentappointment.admin.dto.response.doctor.DoctorDropdownDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDepartmentResponseDTO extends AuditableResponseDTO implements Serializable {

    private String hospitalId;

    private String hospitalName;

    private Long id;

    private String name;

    private String code;

    private String description;

    private Character status;

    private String remarks;

    private Double appointmentCharge,followUpCharge;

    private List<DoctorDropdownDTO> doctorList;

    private List<DropDownResponseDTO> roomList;

}
