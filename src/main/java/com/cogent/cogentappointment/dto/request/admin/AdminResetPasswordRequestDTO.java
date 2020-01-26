package com.cogent.cogentappointment.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 19/01/2020
 */
@Getter
@Setter
public class AdminResetPasswordRequestDTO implements Serializable {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String remarks;
}
