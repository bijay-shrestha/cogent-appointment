package com.cogent.cogentappointment.client.dto.response.admin;

import com.cogent.cogentappointment.client.dto.response.clientIntegration.ClientIntegrationResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author smriti ON 01/01/2020
 */
@Getter
@Setter
public class AdminLoggedInInfoResponseDTO implements Serializable {

    private Long adminId;

    private String email;

    private String fullName;

    private String fileUri;

    private Long departmentId;

    private String departmentName;

    private Long profileId;

    private String profileName;

    private String hospitalName;

    private String hospitalLogo;

    private Character isAllRoleAssigned;

    private Character isSideBarCollapse;

    private ClientIntegrationResponseDTO eCIntegrate;

    Map<String, String> requestBody;
}
