package com.cogent.cogentappointment.dto.request.department;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentSearchRequestDTO {
    private Long id;

    private String name;

    private String code;

    private Character status;
}
