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
public class DepartmentRequestDTO implements Serializable {

    @NotNull
    @NotBlank
    @NotEmpty
    private String name;

    @NotNull
    @NotBlank
    @NotEmpty
    private String departmentCode;

    @NotNull
    private Long hospitalId;

    @Status
    private Character status;
}
