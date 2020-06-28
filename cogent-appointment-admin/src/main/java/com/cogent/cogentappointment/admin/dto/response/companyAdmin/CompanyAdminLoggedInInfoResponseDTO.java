package com.cogent.cogentappointment.admin.dto.response.companyAdmin;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author smriti ON 01/01/2020
 */
@Getter
@Setter
public class CompanyAdminLoggedInInfoResponseDTO implements Serializable {

    private Long adminId;

    private String email;

    private String fullName;

    private String fileUri;

    private Long profileId;

    private String profileName;

    private Long hospitalId;

    private String hospitalName;

    private String hospitalLogo;

    private Character isCompany;

    private Character isAllRoleAssigned;

    private Character isSideBarCollapse;

    private List<Long> favouriteUserMenuId;

    Map<String, String> requestBody;

    Map<String, List<?>> apiIntegration;
}
