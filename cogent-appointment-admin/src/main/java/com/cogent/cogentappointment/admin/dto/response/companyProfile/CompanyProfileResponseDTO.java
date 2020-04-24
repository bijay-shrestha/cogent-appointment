package com.cogent.cogentappointment.admin.dto.response.companyProfile;

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
public class CompanyProfileResponseDTO extends AuditableResponseDTO implements Serializable{

    private String name;

    private String description;

    private Character status;

    private Long companyId;

    private String companyName;

    private String remarks;
}
