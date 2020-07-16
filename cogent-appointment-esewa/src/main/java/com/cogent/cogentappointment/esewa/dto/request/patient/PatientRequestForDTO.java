package com.cogent.cogentappointment.esewa.dto.request.patient;

import com.cogent.cogentappointment.esewa.constraintvalidator.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 26/02/20
 */
@Getter
@Setter
public class PatientRequestForDTO implements Serializable {

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    private String mobileNumber;

    @NotNull
    @Gender
    private Character gender;

    @NotNull
    private Date dateOfBirth;

    private String email;

    private String address;

    private Long provinceId;

    private Long vdcOrMunicipalityId;

    private Long districtId;

    private String wardNumber;
}
