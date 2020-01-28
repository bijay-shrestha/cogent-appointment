package com.cogent.cogentappointment.client.dto.request.patient;

import com.cogent.cogentappointment.client.constraintvalidator.Status;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 18/01/2020
 */
@Getter
@Setter
public class PatientSearchRequestDTO implements Serializable {
    @NotNull
    private String esewaId;

    @Status
    private Character isSelf;

    @NotNull
    private Long hospitalId;
}
