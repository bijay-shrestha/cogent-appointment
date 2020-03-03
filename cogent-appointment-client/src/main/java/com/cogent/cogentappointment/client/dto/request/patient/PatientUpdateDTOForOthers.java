package com.cogent.cogentappointment.client.dto.request.patient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 03/03/20
 */
@Getter
@Setter
public class PatientUpdateDTOForOthers implements Serializable {

    @NotNull
    private Long hospitalPatientInfoId;

    @NotNull
    private String name;

    @NotNull
    private String mobileNumber;

    @NotNull
    private Character gender;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    private String email;

    @NotNull
    private String address;
}
