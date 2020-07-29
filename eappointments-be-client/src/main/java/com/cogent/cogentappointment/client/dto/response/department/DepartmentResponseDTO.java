package com.cogent.cogentappointment.client.dto.response.department;

import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO extends AuditableResponseDTO implements Serializable {

    private String name;

    private String departmentCode;

    private Character status;

    private String remarks;

}
