package com.cogent.cogentappointment.admin.dto.request.department;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
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
    @NotEmpty
    @NotBlank
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
    @NotEmpty
    @NotBlank
    private Long hospitalId;

    @NotNull
    @Status
    private Character status;

    @NotNull
    @NotEmpty
    @NotBlank
    private String remarks;
}
