package com.cogent.cogentappointment.client.dto.request.qualification;

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

    private Long qualificationId;

    private Long universityId;

    private Long qualificationAliasId;

    private Character status;
}
