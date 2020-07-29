package com.cogent.cogentappointment.client.dto.response.profile;

import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 7/15/19
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponseDTO extends AuditableResponseDTO implements Serializable {

    private String name;

    private String description;

    private Character status;

    private Long departmentId;

    private String departmentName;

    private String remarks;

    private Character isAllRoleAssigned;
}
