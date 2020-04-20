package com.cogent.cogentappointment.admin.dto.request.CompanyAdmin;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminDashboardRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.admin.AdminMacAddressInfoUpdateRequestDTO;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author smriti on 2019-08-30
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyAdminUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

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
    private Character genderCode;

    @NotNull
    private Long companyId;

    @NotNull
    private Long profileId;

    @NotNull
    @NotEmpty
    private String remarks;

    private Character isAvatarUpdate;

    private List<AdminMacAddressInfoUpdateRequestDTO> macAddressUpdateInfo;

    private List<AdminDashboardRequestDTO> adminDashboardRequestDTOS;

    private String baseUrl;
}
