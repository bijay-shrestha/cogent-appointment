package com.cogent.cogentappointment.admin.dto.response.profile;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
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
public class ProfileResponseDTO extends AuditableResponseDTO implements Serializable{

    private String name;

    private String description;

    private Character status;

    private Long departmentId;

    private String departmentName;

    private Long hospitalId;

    private String hospitalName;

    private String remarks;

    private String hospitalAlias;
}
