package com.cogent.cogentappointment.admin.dto.request.admin;

import lombok.*;

import java.io.Serializable;

/**
 * @author smriti on 2019-08-26
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminSearchRequestDTO implements Serializable {

    private Long adminMetaInfoId;

    private Long hospitalId;

    private Long departmentId;

    private Long profileId;

    private Character status;

    private Character genderCode;
}
