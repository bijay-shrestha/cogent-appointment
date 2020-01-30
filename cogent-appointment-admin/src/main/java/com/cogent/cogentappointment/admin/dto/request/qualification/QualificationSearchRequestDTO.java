package com.cogent.cogentappointment.admin.dto.request.qualification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author smriti on 11/11/2019
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualificationSearchRequestDTO implements Serializable {

    private String name;

    private Long universityId;

    private Long countryId;
}
