package com.cogent.cogentappointment.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 01/01/2020
 */
@Getter
@Setter
public class AdminInfoRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String subDepartmentCode;
}
