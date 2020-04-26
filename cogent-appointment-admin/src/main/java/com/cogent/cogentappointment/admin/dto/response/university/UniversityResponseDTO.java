package com.cogent.cogentappointment.admin.dto.response.university;

import com.cogent.cogentappointment.admin.dto.response.commons.AuditableResponseDTO;
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
public class UniversityResponseDTO extends AuditableResponseDTO implements Serializable {

    private String name;

    private String address;

    private String countryName;

    private Long countryId;

    private Character status;

    private String remarks;
}
