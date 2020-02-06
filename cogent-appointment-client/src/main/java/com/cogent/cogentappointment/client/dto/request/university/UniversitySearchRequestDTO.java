package com.cogent.cogentappointment.client.dto.request.university;

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
public class UniversitySearchRequestDTO implements Serializable {

    private Long universityId;

    private Long countryId;

    private Character status;
}
