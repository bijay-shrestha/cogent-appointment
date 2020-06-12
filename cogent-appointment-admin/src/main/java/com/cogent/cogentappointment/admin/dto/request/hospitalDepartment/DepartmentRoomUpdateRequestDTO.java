package com.cogent.cogentappointment.admin.dto.request.hospitalDepartment;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 24/01/2020
 */
@Getter
@Setter
public class DepartmentRoomUpdateRequestDTO implements Serializable {

    @NotNull
    private Long roomId;

    @Status
    private Character status;
}
