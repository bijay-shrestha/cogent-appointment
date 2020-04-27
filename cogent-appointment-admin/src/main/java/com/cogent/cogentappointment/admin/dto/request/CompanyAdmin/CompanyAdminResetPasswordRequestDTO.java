package com.cogent.cogentappointment.admin.dto.request.CompanyAdmin;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 19/01/2020
 */
@Getter
@Setter
public class CompanyAdminResetPasswordRequestDTO implements Serializable {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String remarks;
}
