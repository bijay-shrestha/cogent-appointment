package com.cogent.cogentappointment.admin.dto.response.department;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO extends AuditableResponseDTO implements Serializable {

    private String name;

    private String departmentCode;

    private Long hospitalId;

    private String hospitalName;

    private String hospitalAlias;

    private Character status;

    private String remarks;
}
