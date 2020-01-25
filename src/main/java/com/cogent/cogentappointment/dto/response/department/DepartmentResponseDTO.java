package com.cogent.cogentappointment.dto.response.department;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO implements Serializable {

    private String name;

    private String departmentCode;

    private Character status;

    private String remarks;
}
