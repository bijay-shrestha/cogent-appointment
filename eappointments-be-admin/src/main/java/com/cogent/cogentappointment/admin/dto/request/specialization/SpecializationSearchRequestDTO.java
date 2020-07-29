package com.cogent.cogentappointment.admin.dto.request.specialization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-25
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationSearchRequestDTO implements Serializable {

    private String name;

    private String code;

    private Character status;

    private Long hospitalId;
}
