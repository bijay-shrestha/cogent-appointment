package com.cogent.cogentappointment.admin.dto.response.admin;

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

    private String fileUri;

    private Long departmentId;

    private String departmentName;

    private Long profileId;

    private String profileName;

    private Long hospitalId;

    private String hospitalName;

    private Character isCompany;

    private Character isAllRoleAssigned;
}
