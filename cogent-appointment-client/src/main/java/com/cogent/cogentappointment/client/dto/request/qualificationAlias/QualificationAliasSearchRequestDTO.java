package com.cogent.cogentappointment.client.dto.request.qualificationAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Rupak
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualificationAliasSearchRequestDTO implements Serializable {

    private Long qualificationAliasId;

    private Character status;
}
