package com.cogent.cogentappointment.client.dto.request.hospitalDepartment;

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
public class RoomUpdateRequestDTO implements Serializable {

    @NotNull
    private Long roomId;

    @Status
    private Character status;
}
