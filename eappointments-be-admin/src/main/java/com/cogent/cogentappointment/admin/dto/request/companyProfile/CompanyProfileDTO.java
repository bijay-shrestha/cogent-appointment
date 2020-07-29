package com.cogent.cogentappointment.admin.dto.request.companyProfile;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 7/8/19
 */
@Getter
@Setter
@ToString
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

    @NotNull
    private Character isAllRoleAssigned;
}
