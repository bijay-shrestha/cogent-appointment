package com.cogent.cogentappointment.client.dto.response.department;

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

    private Long hospitalId;

    private String hospitalName;

    private Character status;

    private String remarks;
}
