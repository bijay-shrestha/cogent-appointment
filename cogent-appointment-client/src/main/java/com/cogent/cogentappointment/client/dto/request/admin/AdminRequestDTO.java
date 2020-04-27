package com.cogent.cogentappointment.client.dto.request.admin;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
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
public class AdminRequestDTO implements Serializable {

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
    private Long profileId;

    private List<String> macAddressInfo;

    private List<AdminDashboardRequestDTO> adminDashboardRequestDTOS;

    private String password;

    private String baseUrl;
}
