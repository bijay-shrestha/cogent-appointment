package com.cogent.cogentappointment.admin.dto.request.companyProfile;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 7/8/19
 */
@Getter
@Setter
public class CompanyProfileDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    private Long companyId;

    @NotNull
    @Status
    private Character status;
}
