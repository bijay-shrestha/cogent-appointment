package com.cogent.cogentappointment.client.dto.request.admin;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
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
public class AdminUpdateRequestDTO implements Serializable {

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
    private Long profileId;

    @NotNull
    @NotEmpty
    private String remarks;

    /*Y-> NEW AVATAR IS UPDATED
    * N-> AVATAR IS SAME AS BEFORE. SO IF IT IS 'N', THEN NO NEED TO UPDATE AVATAR
    * */
    private Character isAvatarUpdate;

    private List<AdminMacAddressInfoUpdateRequestDTO> macAddressUpdateInfo;

    private List<AdminDashboardRequestDTO> adminDashboardRequestDTOS;
}
