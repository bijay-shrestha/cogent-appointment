package com.cogent.cogentappointment.esewa.dto.request.patient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti ON 08/02/2020
 */
@Getter
@Setter
public class PatientMinSearchRequestDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String mobileNumber;

    @NotNull
    private Date dateOfBirth;
}
