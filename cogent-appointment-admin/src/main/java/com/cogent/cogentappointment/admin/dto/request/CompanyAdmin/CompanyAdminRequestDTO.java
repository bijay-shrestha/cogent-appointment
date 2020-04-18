package com.cogent.cogentappointment.admin.dto.request.CompanyAdmin;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminDashboardRequestDTO;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 6/25/19
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyAdminRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String fullName;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String mobileNumber;

    @NotNull
    @Status
    private Character status;

    @NotNull
    private Character hasMacBinding;

    @NotNull
    private Long companyId;

    @NotNull
    private Character genderCode;

    @NotNull
    private Long profileId;

    private Character isSideBarCollapse;

    private List<String> macAddressInfo;

    private List<AdminDashboardRequestDTO> adminDashboardRequestDTOS;

    private String baseUrl;
}
