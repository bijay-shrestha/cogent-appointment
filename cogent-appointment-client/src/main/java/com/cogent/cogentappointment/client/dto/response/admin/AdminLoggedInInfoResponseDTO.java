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

    private String fileUri;

    private Long departmentId;

    private String departmentName;

    private Long profileId;

    private String profileName;

    private String hospitalName;

    private Character isSideBarCollapse;
}
