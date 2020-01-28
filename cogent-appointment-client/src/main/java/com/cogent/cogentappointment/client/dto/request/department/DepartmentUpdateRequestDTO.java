package com.cogent.cogentappointment.client.dto.request.department;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
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
    private Long hospitalId;

    @NotNull
    @Status
    private Character status;

    @NotNull
    private String remarks;
}
