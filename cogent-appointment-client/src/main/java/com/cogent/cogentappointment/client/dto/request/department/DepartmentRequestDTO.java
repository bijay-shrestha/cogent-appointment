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
public class DepartmentRequestDTO implements Serializable {

    @NotNull
    private String name;

    @NotNull
    private String departmentCode;

    @NotNull
    private Long hospitalId;

    @Status
    private Character status;
}
