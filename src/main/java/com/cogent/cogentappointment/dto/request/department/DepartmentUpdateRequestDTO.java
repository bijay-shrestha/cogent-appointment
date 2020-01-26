package com.cogent.cogentappointment.dto.request.department;

import com.cogent.cogentappointment.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

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
    private String name;

    @NotNull
    private String departmentCode;

    @NotNull
    private String hospitalId;

    @NotNull
    @Status
    private Character status;

    @NotNull
    private String remarks;
}
