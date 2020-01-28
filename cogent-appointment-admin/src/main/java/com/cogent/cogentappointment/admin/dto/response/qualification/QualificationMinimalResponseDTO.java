package com.cogent.cogentappointment.admin.dto.response.qualification;

import lombok.*;

import java.io.Serializable;
/**
 * @author smriti on 11/11/2019
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QualificationMinimalResponseDTO implements Serializable {

    private Long id;

    private String name;

    private String university;

    private String countryName;

    private String qualificationAliasName;

    private Character status;

    private int totalItems;

}
