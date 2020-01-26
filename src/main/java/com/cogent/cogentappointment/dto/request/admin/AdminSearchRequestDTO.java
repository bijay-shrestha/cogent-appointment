package com.cogent.cogentappointment.dto.request.admin;

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

    private Character status;

    private Long adminCategoryId;

    private Long profileId;
}
