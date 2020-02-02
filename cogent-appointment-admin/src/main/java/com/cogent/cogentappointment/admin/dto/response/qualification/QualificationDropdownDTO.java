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
public class QualificationDropdownDTO implements Serializable {
    private Long id;

    private String qualificationName;

    private String universityName;

    private String qualificationAliasName;
}
