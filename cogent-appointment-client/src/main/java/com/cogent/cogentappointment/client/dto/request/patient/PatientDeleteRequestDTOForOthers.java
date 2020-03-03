package com.cogent.cogentappointment.client.dto.request.patient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author smriti on 03/03/20
 */
@Getter
@Setter
public class PatientDeleteRequestDTOForOthers implements Serializable {

    @NotNull
    private Long parentPatientId;

    @NotNull
    private Long childPatientId;
}
