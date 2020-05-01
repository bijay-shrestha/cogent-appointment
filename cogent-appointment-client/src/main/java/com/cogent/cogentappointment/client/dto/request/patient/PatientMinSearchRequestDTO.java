package com.cogent.cogentappointment.client.dto.request.patient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String mobileNumber;

    @NotNull
    private Date dateOfBirth;

    @NotNull
    private Long hospitalId;
}
