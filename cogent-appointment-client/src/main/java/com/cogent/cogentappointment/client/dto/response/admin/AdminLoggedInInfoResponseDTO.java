package com.cogent.cogentappointment.client.dto.response.admin;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author smriti ON 01/01/2020
 */
@Getter
@Setter
public class AdminLoggedInInfoResponseDTO implements Serializable {

    private Long adminId;

    private String username;

    private String fullName;

    private Long departmentId;

    private Long subDepartmentId;

    private String subDepartmentName;

    private Long profileId;

    private String profileName;
}
