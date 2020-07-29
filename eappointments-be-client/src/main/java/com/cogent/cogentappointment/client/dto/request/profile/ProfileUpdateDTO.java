package com.cogent.cogentappointment.client.dto.request.profile;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 2019-09-11
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateDTO implements Serializable {
    @NotNull
    private Long id;

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
    @NotEmpty
    private String remarks;

    @NotNull
    @Status
    private Character isAllRoleAssigned;
}
