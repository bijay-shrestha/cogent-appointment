package com.cogent.cogentappointment.admin.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 19/01/2020
 */
@Getter
@Setter
public class AdminResetPasswordRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String password;

    @NotNull
    @NotBlank
    @NotEmpty
    private String remarks;
}