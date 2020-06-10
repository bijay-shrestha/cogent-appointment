package com.cogent.cogentappointment.admin.dto.request.hospital;

import com.cogent.cogentappointment.admin.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 12/01/2020
 */
@Getter
@Setter
public class HospitalContactNumberUpdateRequestDTO implements Serializable {

    private Long hospitalContactNumberId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String contactNumber;

    @Status
    private Character status;
}
