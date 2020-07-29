package com.cogent.cogentappointment.admin.dto.request.profile;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti on 2019-09-11
 */
@Getter
@Setter
public class ProfileSearchRequestDTO implements Serializable {

    private Long profileId;

    private Character status;

    private Long departmentId;

    private Long hospitalId;
}
