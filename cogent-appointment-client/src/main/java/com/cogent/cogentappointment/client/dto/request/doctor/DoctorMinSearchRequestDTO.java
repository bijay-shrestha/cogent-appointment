package com.cogent.cogentappointment.client.dto.request.doctor;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti ON 04/02/2020
 */
@Getter
@Setter
public class DoctorMinSearchRequestDTO implements Serializable {

    @NotNull
    private Long hospitalId;

    private Long specializationId;

    private Long doctorId;
}
