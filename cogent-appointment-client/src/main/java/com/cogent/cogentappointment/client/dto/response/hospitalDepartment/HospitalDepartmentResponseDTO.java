package com.cogent.cogentappointment.client.dto.response.hospitalDepartment;

import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalDepartmentResponseDTO extends AuditableResponseDTO implements Serializable {

    private String name;

    private String code;

    private String description;

    private Character status;

    private String remarks;

    private Double appointmentCharge,followUpCharge;

    private List<String> doctorList;

    private List<String> roomList;

}
