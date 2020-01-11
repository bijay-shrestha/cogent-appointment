package com.cogent.cogentappointment.dto.response.qualification;

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
public class QualificationDropdownDTO implements Serializable {
    private Long id;

    private String name;

    private String university;

    private String countryName;

    private String qualificationAliasName;
}
