package com.cogent.cogentappointment.client.dto.request.department;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
public class DepartmentUpdateRequestDTO implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String departmentCode;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    @NotBlank
    private String remarks;
}
