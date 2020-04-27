package com.cogent.cogentappointment.admin.dto.request.CompanyAdmin;

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
public class CompanyAdminInfoRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String email;
}
