package com.cogent.cogentappointment.client.dto.response.admin;

import lombok.*;

import java.io.Serializable;

/**
 * @author Sauravi Thapa 1/3/20
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSubDepartmentResponseDTO implements Serializable {

    private Long subDepartmentId;

    private String subDepartmentName;

    private String subDepartmentCode;
}
