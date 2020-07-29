package com.cogent.cogentappointment.client.dto.request.profile;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
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
public class ProfileDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    @Status
    private Character status;

    @NotNull
    private Long departmentId;

    @NotNull
    private Character isAllRoleAssigned;
}
