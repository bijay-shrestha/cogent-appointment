package com.cogent.cogentappointment.client.dto.response.department;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentMinimalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String departmentCode;

    private Character status;

    private Integer totalItems;
}
