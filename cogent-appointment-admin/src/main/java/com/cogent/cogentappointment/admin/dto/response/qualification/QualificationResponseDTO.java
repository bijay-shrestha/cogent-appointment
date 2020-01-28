package com.cogent.cogentappointment.admin.dto.response.qualification;

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
public class QualificationResponseDTO implements Serializable {

    private String name;

    private String university;

    private Long countryId;

    private String countryName;

    private Long qualificationAliasId;

    private String qualificationAliasName;

    private Character status;

    private String remarks;
}
