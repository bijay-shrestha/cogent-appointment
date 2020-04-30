package com.cogent.cogentappointment.client.dto.response.qualification;

import com.cogent.cogentappointment.client.dto.response.common.AuditableResponseDTO;
import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 11/11/2019
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualificationResponseDTO extends AuditableResponseDTO implements Serializable {

    private String name;

    private Long universityId;

    private String universityName;

    private Long qualificationAliasId;

    private String qualificationAliasName;

    private Character status;

    private String remarks;
}
