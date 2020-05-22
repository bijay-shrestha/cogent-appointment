package com.cogent.cogentappointment.admin.dto.response.hospitalDepartment;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDepartmentResponseDTO extends AuditableResponseDTO implements Serializable {

    private String hospitalName;

    private String name;

    private String code;

    private String description;

    private Character status;

    private String remarks;

    private Double appointmentCharge,followUpCharge;

    private List<String> doctorList;

    private List<String> roomList;

}
