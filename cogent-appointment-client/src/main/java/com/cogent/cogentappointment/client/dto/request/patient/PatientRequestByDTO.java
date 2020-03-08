package com.cogent.cogentappointment.client.dto.request.patient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 26/02/20
 */
@Getter
@Setter
public class PatientRequestByDTO implements Serializable {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String mobileNumber;

    @NotNull
    private Character gender;

    @NotNull
    private Date dateOfBirth;

    private String email;

    private String eSewaId;

    private String address;
}
