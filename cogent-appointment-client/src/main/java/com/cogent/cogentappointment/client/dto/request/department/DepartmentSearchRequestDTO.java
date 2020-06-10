package com.cogent.cogentappointment.client.dto.request.department;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
public class DepartmentSearchRequestDTO implements Serializable {
    private Long id;

    private String departmentCode;

    private Character status;
}
